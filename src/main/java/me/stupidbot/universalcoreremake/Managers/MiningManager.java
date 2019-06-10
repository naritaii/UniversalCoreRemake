package me.stupidbot.universalcoreremake.Managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import de.slikey.effectlib.Effect;
import me.stupidbot.universalcoreremake.Effects.BlockBreak;
import me.stupidbot.universalcoreremake.Effects.BlockRegen;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.BlockUtils;
import me.stupidbot.universalcoreremake.Utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MiningManager implements Listener {
    private Map<UUID, Block> miningPlayers = new HashMap<>();
    private Map<Block, Integer> regen = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) { // Stop player from breaking blocks so we can handle block breaking
        Player p = e.getPlayer();
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 255,
                true, false), true);
    }

    @EventHandler
    public void onChangeItemHeld(PlayerItemHeldEvent e) {
        removeMiningPlayer(e.getPlayer());
    }

    public void initialize() {
        // Click Listener
        UniversalCoreRemake.getProtocolManager().addPacketListener(new PacketAdapter(UniversalCoreRemake.getInstance(),
                ListenerPriority.MONITOR,
                PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                if (e.getPacketType() == PacketType.Play.Client.BLOCK_DIG) {
                    PacketContainer packet = e.getPacket();
                    EnumWrappers.PlayerDigType c = packet.getPlayerDigTypes().read(0);
                    Player p = e.getPlayer();

                    if (c == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                        Block b = packet.getBlockPositionModifier().read(0).toLocation(p.getWorld())
                                .getBlock();
                        if (b.getType() != Material.BARRIER &&
                                UniversalCoreRemake.getBlockMetadataManager().hasMetadata(b, "MINEABLE"))
                            putMiningPlayer(p, b);
                    } else if (c == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK ||
                            c == EnumWrappers.PlayerDigType.DROP_ALL_ITEMS ||
                            c == EnumWrappers.PlayerDigType.DROP_ITEM)
                        removeMiningPlayer(p);
                }
            }
        });

        Map<UUID, Integer> timer = new HashMap<>();

        // Main Runnable
        Bukkit.getScheduler().runTaskTimerAsynchronously(UniversalCoreRemake.getInstance(), () -> {
            // Cleanup timer
            timer.keySet().stream().filter(id -> !miningPlayers.containsKey(id)).collect(Collectors.toList())
                    .forEach(timer::remove);

            for (UUID id : miningPlayers.keySet()) { // Block being mined
                Player p = Bukkit.getPlayer(id);
                Block b = miningPlayers.get(id);
                int d = timer.getOrDefault(id, 0) + 1;
                int finishedInt = (int) (MineableBlock.valueOf(b.getType().toString()).getDurability() * 20);

                // Block Break Animation
                int stage = (int) Math.floor((d * 10d) / finishedInt);
                PacketContainer breakAnim = UniversalCoreRemake.getProtocolManager().createPacket(
                        PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
                breakAnim.getBlockPositionModifier().write(0, new BlockPosition(b.getX(), b.getY(),
                        b.getZ()));
                breakAnim.getIntegers().write(0, BlockUtils.getBlockEntityId(b));
                breakAnim.getIntegers().write(1, stage);
                try {
                    UniversalCoreRemake.getProtocolManager().sendServerPacket(p, breakAnim);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                if (d > finishedInt) { // Finished Mining
                    MineableBlock mb = MineableBlock.valueOf(b.getType().toString());
                    Effect eff = new BlockBreak(UniversalCoreRemake.getEffectManager());
                    eff.material = b.getType();
                    eff.setLocation(b.getLocation());
                    eff.run();

                    p.getInventory().addItem(new ItemStack((mb.getLoot()))); // TODO Make util for dropping item if
                                                                             // TODO Inventory is full.
                    PlayerLevelling.giveXp(p, mb.getBaseXp());
                    TextUtils.sendActionbar(p, "&3XP: &a+" + mb.getBaseXp() +
                            " &f" + TextUtils.capitalizeFully(b.getType().toString()) + ": &a+1");

                    timer.remove(id);
                    putRegenningBlock(b, (int) (mb.getGetRegenerateTime() * 20));
                    removeMiningPlayer(p);
                    b.setType(Material.BARRIER);
                } else
                    timer.put(id, d);
            }

            for (Block b : regen.keySet()) { // Block has been mined
                int i = regen.get(b) - 1;

                if (i < 1) { // Regen Block
                    b.setType(Material.valueOf(
                            UniversalCoreRemake.getBlockMetadataManager().getMetadata(b, "MINEABLE")));
                    Effect eff = new BlockRegen(UniversalCoreRemake.getEffectManager());
                    eff.setLocation(b.getLocation());
                    eff.run();
                    removeRegenningBlock(b);
                } else
                    putRegenningBlock(b, i);
            }
        }, 0, 1);
    }

    public void disable() {
        miningPlayers.clear();
        regen.forEach((Block b, Integer i) -> {
            b.getChunk().load();
            b.setType(Material.valueOf(
                    UniversalCoreRemake.getBlockMetadataManager().getMetadata(b, "MINEABLE")));
            regen.remove(b);
        });
    }

    private void putMiningPlayer(Player p, Block b) {
        miningPlayers.put(p.getUniqueId(), b);
    }

    private void removeMiningPlayer(Player p) {
        UUID id = p.getUniqueId();
        if (miningPlayers.containsKey(id)) {
            Block b = miningPlayers.get(id);
            PacketContainer breakAnim = UniversalCoreRemake.getProtocolManager().createPacket(
                    PacketType.Play.Server.BLOCK_BREAK_ANIMATION);

            breakAnim.getBlockPositionModifier().write(0, new BlockPosition(b.getX(), b.getY(),
                    b.getZ()));
            breakAnim.getIntegers().write(0, BlockUtils.getBlockEntityId(b));
            breakAnim.getIntegers().write(1, 10);
            try {
                UniversalCoreRemake.getProtocolManager().sendServerPacket(p, breakAnim);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


            miningPlayers.remove(id);
        }
    }

    private void putRegenningBlock(Block b, int i) {
        regen.put(b, i);
    }

    private void removeRegenningBlock(Block b) {
        regen.remove(b);
    }

    enum MineableBlock {
        RED_SANDSTONE(6.5d, 6.5d, Material.SANDSTONE, 0.05f, 1,
                Material.RED_SANDSTONE),
        SANDSTONE(5d, 0d, Material.RED_SANDSTONE, 0.5f, 3,
                Material.SANDSTONE);

        private final double durability;
        private final double regenerateTime;
        private final Material enhanceBlock;
        private final float enhanceChance;
        private final int baseXp;
        private final Material loot;

        /**
         * @param durability Time in seconds block takes to mine with hand and no "modifiers".
         * @param regenerateTime Time in seconds block takes to regenerate.
         * @param enhanceBlock Block to turn into when enhanced.
         * @param enhanceChance Chance of turning block into enhanceBlock.
         * @param baseXp XP given without any multipliers.
         * @param loot Item dropped.
         */
        MineableBlock(Double durability, Double regenerateTime, Material enhanceBlock, Float enhanceChance,
                      int baseXp, Material loot) {
            this.durability = durability;
            this.regenerateTime = regenerateTime;
            this.enhanceBlock = enhanceBlock;
            this.enhanceChance = enhanceChance;
            this.baseXp = baseXp;
            this.loot = loot;
        }

        double getDurability() {
            return durability;
        }

        double getGetRegenerateTime() {
            return regenerateTime;
        }

        Material getEnhanceBlock() {
            return enhanceBlock;
        }

        float getEnhanceChance() {
            return enhanceChance;
        }

        int getBaseXp() {
            return baseXp;
        }

        Material getLoot() {
            return loot;
        }
    }
}