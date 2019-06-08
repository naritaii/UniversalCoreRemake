package me.stupidbot.universalcoreremake.Managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.BlockUtils;
import me.stupidbot.universalcoreremake.Utilities.PlayerLevelling;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningManager implements Listener {
    private Map<UUID, Block> miningPlayers = new HashMap<>();
    private Map<Block, Integer> regen = new HashMap<>();
    private BukkitTask miningTask;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) { // Stop player from breaking blocks so we can handle block breaking
        Player p = e.getPlayer();
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 255,
                true, false), true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (e.getAction() == Action.LEFT_CLICK_BLOCK && b.getType() != Material.BARRIER &&
                UniversalCoreRemake.getBlockMetadataManager().hasMetadata(b, "MINEABLE"))
            putMiningPlayer(p, b);
    }

    @EventHandler
    public void onPlayerChangeItemHeld(PlayerItemHeldEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        miningPlayers.remove(id);
    }

    public void initialize() {
        Map<UUID, Integer> timer = new HashMap<>();
        miningTask = new BukkitRunnable() {
            @Override
            public void run() {
                timer.forEach((UUID id, Integer i) -> {
                    if (!miningPlayers.containsKey(id))
                        timer.remove(id);
                });
                for (UUID id : miningPlayers.keySet()) { // Block being mined
                    Player p = Bukkit.getPlayer(id);
                    Block b = miningPlayers.get(id);
                    int d = timer.getOrDefault(id, 0) + 1;
                    int finishedInt = (int) (MineableBlock.valueOf(b.getType().toString()).getDurability() * 20);

                    // Block Animation
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

                    if (d > finishedInt) {
                        timer.remove(id);
                        MineableBlock mb = MineableBlock.valueOf(b.getType().toString());
                        putRegenningBlock(b, (int) (mb.getGetRegenerateTime() * 20));
                        removeMiningPlayer(p);
                        b.setType(Material.BARRIER);
                        PlayerLevelling.giveXp(p, mb.getBaseXp());
                    } else
                        timer.put(id, d);
                }

                for (Block b : regen.keySet()) { // Block has been mined
                    int i = regen.get(b) - 1;

                    if (i < 1) {
                        b.setType(Material.valueOf(
                                UniversalCoreRemake.getBlockMetadataManager().getMetadata(b, "MINEABLE")));
                        removeRegenningBlock(b);
                    } else
                        putRegenningBlock(b, i);
                }
            }
        }.runTaskTimer(UniversalCoreRemake.getInstance(), 0, 1);
    }

    public void disable() {
        miningTask.cancel();
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
        miningPlayers.remove(p.getUniqueId());
    }

    private void putRegenningBlock(Block b, int i) {
        regen.put(b, i);
    }

    private void removeRegenningBlock(Block b) {
        regen.remove(b);
    }

    enum MineableBlock {
        RED_SANDSTONE(6.5d, 3d, Material.SANDSTONE, 0.05f, 1,
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