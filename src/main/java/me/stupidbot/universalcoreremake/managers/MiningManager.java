package me.stupidbot.universalcoreremake.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.slikey.effectlib.Effect;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.effects.BlockBreak;
import me.stupidbot.universalcoreremake.effects.BlockRegen;
import me.stupidbot.universalcoreremake.effects.EnhancedBlockBreak;
import me.stupidbot.universalcoreremake.events.UniversalBlockBreakEvent;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.LocationUtils;
import me.stupidbot.universalcoreremake.utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.utilities.Stamina;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class MiningManager implements Listener {
    public MiningManager() {
        initialize();
    }

    private final Map<UUID, Block> miningPlayers = new HashMap<>();
    private final Map<Block, Integer> regen = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) { // Stop player from breaking blocks so we can handle block breaking
        Player p = e.getPlayer();
        UniversalCoreRemake plugin = UniversalCoreRemake.getInstance();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 255,
                        true, false), true));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) { // Stop player from breaking blocks so we can handle block breaking
        Player p = e.getPlayer();
        Bukkit.getScheduler().runTaskLater(UniversalCoreRemake.getInstance(), () ->
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 255,
                        true, false), true), 1);
    }

    @EventHandler
    public void onChangeItemHeld(PlayerItemHeldEvent e) {
        removeMiningPlayer(e.getPlayer());
    }

    private final Material respawnBlock = Material.BEDROCK;

    private void initialize() {
        // Click Listener
        UniversalCoreRemake.getProtocolManager().addPacketListener(new PacketAdapter(UniversalCoreRemake.getInstance(),
                ListenerPriority.MONITOR, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                if (e.getPacketType() == PacketType.Play.Client.BLOCK_DIG) {
                    PacketContainer packet = e.getPacket();
                    EnumWrappers.PlayerDigType c = packet.getPlayerDigTypes().read(0);
                    Player p = e.getPlayer();


                    if (c == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                        Block b = packet.getBlockPositionModifier().read(0).toLocation(p.getWorld())
                                .getBlock();

                        if (b.getType() != respawnBlock)
                            if (UniversalCoreRemake.getBlockMetadataManager().hasMeta(b, "MINEABLE"))
                                putMiningPlayer(p, b);
                            else if (UniversalCoreRemake.UNIVERSAL_MINE != null) { // WorldGuard flag support
                                ApplicableRegionSet regions = UniversalCoreRemake.getWorldGuardPlugin().getRegionManager(b.getWorld())
                                        .getApplicableRegions(b.getLocation());
                                for (ProtectedRegion region : regions) {
                                    @SuppressWarnings("unchecked")
                                    Set<String> s = region.getFlag(UniversalCoreRemake.UNIVERSAL_MINE);

                                    if (s != null && (!s.isEmpty())) {
                                        s = s.stream().map((n) -> n.trim().toUpperCase()).collect(Collectors.toSet());
                                        if (s.contains(b.getType().toString())) {
                                            UniversalCoreRemake.getBlockMetadataManager().setMeta(b,
                                                    "MINEABLE", b.getType().toString());
                                            putMiningPlayer(p, b);
                                        }
                                    }
                                }
                            }

                    } else if (c == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK ||
                            c == EnumWrappers.PlayerDigType.DROP_ALL_ITEMS ||
                            c == EnumWrappers.PlayerDigType.DROP_ITEM)
                        removeMiningPlayer(p);
                }
            }
        });

        Map<UUID, Integer> timer = new HashMap<>();
        // Main Runnable
        Bukkit.getScheduler().runTaskTimer(UniversalCoreRemake.getInstance(), () -> {
            // Cleanup timer
            timer.keySet().stream().filter(id -> !miningPlayers.containsKey(id)).collect(Collectors.toList())
                    .forEach(timer::remove);

            // Block being mined
            for (UUID id : miningPlayers.keySet()) {
                // Setup Vars
                Player p = Bukkit.getPlayer(id);
                Block b = miningPlayers.get(id);
                MineableBlock mb = MineableBlock.valueOf(b.getType().toString());
                int stamina = mb.getBaseStaminaUsage();

                if (Stamina.getStamina(p) < stamina) {
                    removeMiningPlayer(p);
                    TextUtils.sendTitle(p, "", 5, 20, 5);
                    if (Stamina.getMaxStamina(p) >= stamina)
                        TextUtils.sendSubtitle(p, "&c&lEAT TO REGAIN STAMINA", 5, 20, 5);
                    else
                        TextUtils.sendSubtitle(p, "&c&lNOT ENOUGH STAMINA", 5, 20, 5);
                } else {
                    int d = timer.getOrDefault(id, 0) + 1;
                    boolean usingItem = ItemLevelling.getPickaxes().contains(p.getItemInHand().getType());
                    ItemStack itemInHand = p.getItemInHand();
                    float durabilityMod = mb.getDurability() * getItemMultiplier(itemInHand.getType());
                    int finishedInt = (int) ((mb.getDurability() - durabilityMod) * 20);

                    if (d < finishedInt) { // If Still Mining
                        int stage = (int) Math.floor((d * 10f) / finishedInt);
                        breakAnim(p, b, stage);

                        timer.put(id, d);
                    }
                    // If Finished Mining
                    else {
                        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
                        int amt = 1;
                        int xp = mb.getBaseXp();
                        UniversalBlockBreakEvent e = new UniversalBlockBreakEvent(p, b, amt, xp, stamina);
                        Bukkit.getPluginManager().callEvent(e);
                        amt = e.getAmount();
                        xp = e.getXp();
                        stamina = e.getStamina();
                        itemInHand = p.getItemInHand(); // Updating in case it's removed e.g. by an enchant
                        usingItem = ItemLevelling.getPickaxes().contains(p.getItemInHand().getType());


                        // Handling player
                        up.setBlocksMined(up.getBlocksMined() + 1);

                        ItemUtils.addItemSafe(p, new ItemStack(mb.getLoot(), amt));
                        PlayerLevelling.giveXp(p, xp);
                        if (usingItem)
                            itemInHand = ItemLevelling.giveXp(p, itemInHand, xp);
                        Stamina.removeStamina(p, stamina);

                        TextUtils.sendActionbar(p, "&2XP: &a+" + mb.getBaseXp() +
                                " &e" + TextUtils.capitalizeFully(b.getType().toString()) + ": &a+1" +
                                " &3Stamina: &c-" + stamina);


                        // Enhance Block?
                        if (Math.random() < mb.getEnhanceChance())
                            UniversalCoreRemake.getBlockMetadataManager().setMeta(b, "MINEABLE",
                                    mb.getEnhanceBlock().toString());


                        // What To Do On Block Break?
                        switch (mb.getOnBreak()) {
                            case DEFAULT:
                                Effect blockBreak = new BlockBreak(UniversalCoreRemake.getEffectManager());
                                blockBreak.material = b.getType();
                                blockBreak.setLocation(b.getLocation());
                                blockBreak.run();

                                timer.remove(id);
                                putRegenningBlock(b, (int) (mb.getGetRegenerateTime() * 20));
                                b.setType(respawnBlock);
                                removeMiningPlayer(p);
                                break;

                            case INSTANT_RESPAWN:
                                Effect enhancedBlockBreak = new EnhancedBlockBreak(
                                        UniversalCoreRemake.getEffectManager());
                                enhancedBlockBreak.setLocation(b.getLocation());
                                enhancedBlockBreak.run();

                                Effect blockBreak2 = new BlockBreak(UniversalCoreRemake.getEffectManager());
                                blockBreak2.material = b.getType();
                                blockBreak2.setLocation(b.getLocation());
                                blockBreak2.run();

                                timer.put(id, 0);
                                b.setType(Material.valueOf(
                                        UniversalCoreRemake.getBlockMetadataManager().getMeta(b,
                                                "MINEABLE")));
                                break;
                        }
                    }
                }
            }

            Iterator<Map.Entry<Block, Integer>> iter = regen.entrySet().iterator();
            // Block has been mined
            while (iter.hasNext()) {
                Map.Entry<Block, Integer> pair = iter.next();
                Block b = pair.getKey();
                int i = pair.getValue() - 1;

                // Regen Block
                if (i < 1 && UniversalCoreRemake.getBlockMetadataManager().hasMeta(b, "MINEABLE")) {
                    b.setType(Material.valueOf(
                            UniversalCoreRemake.getBlockMetadataManager().getMeta(b, "MINEABLE")));

                    Effect eff = new BlockRegen(UniversalCoreRemake.getEffectManager());
                    eff.setLocation(b.getLocation());
                    eff.run();
                    iter.remove();
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
                    UniversalCoreRemake.getBlockMetadataManager().getMeta(b, "MINEABLE")));
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
            breakAnim(p, b, 10);
            miningPlayers.remove(id);
        }
    }

    private void breakAnim(Player p, Block b, int stage) {
        PacketContainer breakAnim = UniversalCoreRemake.getProtocolManager().createPacket(
                PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        breakAnim.getBlockPositionModifier().write(0, new BlockPosition(b.getX(), b.getY(),
                b.getZ()));
        breakAnim.getIntegers().write(0, LocationUtils.getBlockEntityId(b));
        breakAnim.getIntegers().write(1, stage);
        try {
            UniversalCoreRemake.getProtocolManager().sendServerPacket(p, breakAnim);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void putRegenningBlock(Block b, int i) {
        regen.put(b, i);
    }

//    private void removeRegenningBlock(Block b) {
//        regen.remove(b);
//    }

    private float getItemMultiplier(Material m) {
        float diamondPickMultiplier = 0.75f;
        float goldPickMultiplier = 0.55f;
        float ironPickMultiplier = 0.5f;
        float stonePickMultiplier = 0.4f;
        float woodPickMultiplier = 0.2f;
        switch (m) {
            case WOOD_PICKAXE:
                return woodPickMultiplier;
            case STONE_PICKAXE:
                return stonePickMultiplier;
            case IRON_PICKAXE:
                return ironPickMultiplier;
            case GOLD_PICKAXE:
                return goldPickMultiplier;
            case DIAMOND_PICKAXE:
                return diamondPickMultiplier;
            default:
                return 0f;
        }
    }

    @SuppressWarnings("unused")
    enum MineableBlock {
        RED_SANDSTONE(6.5f, 6.5f, Material.SANDSTONE, 0.075f, 1,
                Material.RED_SANDSTONE, 1, BreakBehavior.DEFAULT),
        SANDSTONE(5.5f, 6.5f, Material.RED_SANDSTONE, 0.4f, 5,
                Material.SANDSTONE, 3, BreakBehavior.INSTANT_RESPAWN),
        COAL_ORE(11.1f, 6.5f, Material.COAL_BLOCK, 0.071f, 7,
                Material.COAL_ORE, 5, BreakBehavior.DEFAULT),
        COAL_BLOCK(13.1f, 6.5f, Material.COAL_ORE, 0.55f, 10,
                Material.COAL_BLOCK, 7, BreakBehavior.INSTANT_RESPAWN),
        IRON_ORE(12.9f, 6.5f, Material.IRON_BLOCK, 0.055f, 9,
                Material.IRON_ORE, 5, BreakBehavior.DEFAULT),
        IRON_BLOCK(14.8f, 6.5f, Material.IRON_ORE, 0.38f, 14,
                Material.IRON_BLOCK, 7, BreakBehavior.INSTANT_RESPAWN),

        // This is to prevent weird nullpointers.
        AIR(Float.MAX_VALUE, 0f, Material.AIR, 0f, 0,
                Material.AIR, 0, BreakBehavior.INSTANT_RESPAWN),
        BEDROCK(Float.MAX_VALUE, 0f, Material.AIR, 0f, 0,
                Material.BEDROCK, 0, BreakBehavior.INSTANT_RESPAWN);

        private final float durability;
        private final float regenerateTime;
        private final Material enhanceBlock;
        private final float enhanceChance;
        private final int baseXp;
        private final Material loot;
        private final int staminaBaseUsage;
        private final BreakBehavior onBreak;

        /**
         * @param durability       Time in seconds block takes to mine with hand and no "modifiers".
         * @param regenerateTime   Time in seconds block takes to regenerate.
         * @param enhanceBlock     Block to turn into when enhanced.
         * @param enhanceChance    Chance of turning block into enhanceBlock.
         * @param baseXp           XP given without any multipliers.
         * @param loot             Item dropped.
         * @param staminaBaseUsage Stamina taken without any multipliers.
         * @param onBreak          What to do when (@link MineableBlock) is broken.
         */
        MineableBlock(float durability, float regenerateTime, Material enhanceBlock, float enhanceChance,
                      int baseXp, Material loot, int staminaBaseUsage, BreakBehavior onBreak) {
            this.durability = durability;
            this.regenerateTime = regenerateTime;
            this.enhanceBlock = enhanceBlock;
            this.enhanceChance = enhanceChance;
            this.baseXp = baseXp;
            this.loot = loot;
            this.onBreak = onBreak;
            this.staminaBaseUsage = staminaBaseUsage;
        }

        float getDurability() {
            return durability;
        }

        float getGetRegenerateTime() {
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

        BreakBehavior getOnBreak() {
            return onBreak;
        }

        int getBaseStaminaUsage() {
            return staminaBaseUsage;
        }
    }

    enum BreakBehavior {
        DEFAULT, INSTANT_RESPAWN
    }
}