package me.stupidbot.universalcoreremake.managers.mining;

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
import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.events.UniversalBlockBreakEvent;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.FileUtils;
import me.stupidbot.universalcoreremake.utilities.LocationUtils;
import me.stupidbot.universalcoreremake.utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class MiningManager implements Listener {
    public MiningManager() {
        reload();
        initialize();
    }

    private final String configPath = UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "mining.yml";
    public void reload() {
        File path = UniversalCoreRemake.getInstance().getDataFolder();
        if (!path.exists())
            //noinspection ResultOfMethodCallIgnored
            path.mkdirs();

        File file = new File(configPath);
        if (!file.exists())
            FileUtils.copy(UniversalCoreRemake.getInstance().getResource("mining.yml"), file);
        FileConfiguration c = YamlConfiguration.loadConfiguration(file);

        registeredMineableBlocks = new ArrayList<>();
        registeredMineableBlocksDictionary = new HashMap<>();

        // There is FileConfiguration.getFloat so we just get the string and parse it
        respawnBlock = Material.getMaterial(c.getString("Mining.RespawnBlock"));
        diamondPickMultiplier = Float.parseFloat(c.getString("Mining.PickaxeMultiplier.Diamond"));
        goldPickMultiplier = Float.parseFloat(c.getString("Mining.PickaxeMultiplier.Gold"));
        ironPickMultiplier = Float.parseFloat(c.getString("Mining.PickaxeMultiplier.Iron"));
        stonePickMultiplier = Float.parseFloat(c.getString("Mining.PickaxeMultiplier.Stone"));
        woodPickMultiplier = Float.parseFloat(c.getString("Mining.PickaxeMultiplier.Wood"));

        for (String s : c.getConfigurationSection("Mining.Blocks").getKeys(false)) {
            Material m = Material.getMaterial(s);

            registeredMineableBlocksDictionary.put(m, registeredMineableBlocks.size());
            registeredMineableBlocks.add(new MineableBlock(
                    m,
                    Float.parseFloat(c.getString("Mining.Blocks." + s + ".BlockDurability")),
                    Float.parseFloat(c.getString("Mining.Blocks." + s + ".RegenerateTime")),
                    Material.getMaterial(c.getString("Mining.Blocks." + s + ".MutateBlock")),
                    Float.parseFloat(c.getString("Mining.Blocks." + s + ".MutateChance")),
                    c.getInt("Mining.Blocks." + s + ".BaseExperience"),
                    Material.getMaterial(c.getString("Mining.Blocks." + s + ".Loot")),
                    c.getInt("Mining.Blocks." + s + ".BaseStaminaUsage"),
                    BreakBehavior.valueOf(c.getString("Mining.Blocks." + s + ".BreakBehavior"))
            ));
        }
    }

    private Material respawnBlock = null;
    private List<MineableBlock> registeredMineableBlocks = null;
    private Map<Material, Integer> registeredMineableBlocksDictionary = null;

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

    private void initialize() {
        // Click Listener
        UniversalCoreRemake.getProtocolManager().addPacketListener(new PacketAdapter(UniversalCoreRemake.getInstance(),
                ListenerPriority.LOW, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                if (e.getPacketType() == PacketType.Play.Client.BLOCK_DIG) {
                    PacketContainer packet = e.getPacket();
                    EnumWrappers.PlayerDigType c = packet.getPlayerDigTypes().read(0);
                    Player p = e.getPlayer();


                    if (c == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                        Block b = packet.getBlockPositionModifier().read(0).toLocation(p.getWorld())
                                .getBlock();

                        if (b.getType() != Material.AIR && !regen.containsKey(b)
                                && registeredMineableBlocksDictionary.containsKey(b.getType()))
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
                                                    "MINEABLE", b.getType().toString().toUpperCase());
                                            UniversalCoreRemake.getBlockMetadataManager().setMeta(b,
                                                    "IN_UNIVERSAL_MINE_REGION", "1");
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
                MineableBlock mb = registeredMineableBlocks.get(registeredMineableBlocksDictionary.get(b.getType()));
                int stamina = mb.getBaseStaminaUsage();

                if (UniversalCoreRemake.getStatsManager().getStamina(p) < stamina) {
                    removeMiningPlayer(p);
                    TextUtils.sendTitle(p, "", 5, 20, 5);
                    if (UniversalCoreRemake.getStatsManager().getMaxStamina(p) >= stamina)
                        TextUtils.sendSubtitle(p, "&c&lEAT TO REGAIN STAMINA", 5, 20, 5);
                    else
                        TextUtils.sendSubtitle(p, "&c&lNOT ENOUGH STAMINA", 5, 20, 5);
                } else {
                    int d = timer.getOrDefault(id, 0) + 1;
                    boolean usingItem = ItemLevelling.getPickaxes().contains(p.getItemInHand().getType());
                    ItemStack itemInHand = p.getItemInHand();
                    float speedMod = getItemMultiplier(itemInHand.getType());
                    if (usingItem) {
                        if (itemInHand.containsEnchantment(UniversalEnchantment.SANDSTONE_LOVER))
                            if (mb.getType() == Material.RED_SANDSTONE || mb.getType() == Material.SANDSTONE)
                                speedMod += 0.05f;
                        if (itemInHand.containsEnchantment(Enchantment.DIG_SPEED))
                            speedMod += 0.2f + (itemInHand.getEnchantmentLevel(Enchantment.DIG_SPEED) * 0.05f);
                    }

                    float durabilityMod = mb.getDurability() * speedMod;
                    int finishedInt = (int) ((mb.getDurability() - durabilityMod) * 20);

                    if (d < finishedInt) { // If Still Mining
                        if (d % 4 == 0) { // Animate lazily
                            int stage = (int) Math.floor((d * 10f) / finishedInt);
                            breakAnim(p, b, stage);
                        }
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
                        if (usingItem) {
                            Map<String, String> meta = ItemMetadata.getMeta(itemInHand);
                            int mined = 0;
                            if (meta.containsKey("BLOCKS_MINED"))
                                mined = Integer.parseInt(meta.get("BLOCKS_MINED"));
                            ItemMetadata.setMeta(itemInHand, "BLOCKS_MINED", ++mined);
                            ItemLevelling.giveXp(p, itemInHand, xp);

                        }
                        UniversalCoreRemake.getStatsManager().removeStamina(p, stamina);

                        TextUtils.sendActionbar(p, "&2XP: &a+" + mb.getBaseXp() +
                                " &e" + TextUtils.capitalizeFully(b.getType().toString()) + ": &a+1" +
                                " &3Stamina: &c-" + stamina);


                        // Enhance Block?
                        float enhanceChance = mb.getEnhanceChance();
                        if (usingItem)
                            if (mb.getOnBreak() != BreakBehavior.INSTANT_RESPAWN &&
                                    itemInHand.containsEnchantment(UniversalEnchantment.SANDSTONE_LOVER))
                                enhanceChance += 0.05f;
                        if (Math.random() < enhanceChance)
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

                    if (UniversalCoreRemake.getBlockMetadataManager().hasMeta(b, "IN_UNIVERSAL_MINE_REGION")) { // If it's in a mining region we don't need to store that its mineable
                        UniversalCoreRemake.getBlockMetadataManager().setMeta(b, "IN_UNIVERSAL_MINE_REGION", null);
                        UniversalCoreRemake.getBlockMetadataManager().setMeta(b, "MINEABLE", null);
                    }

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
            if (UniversalCoreRemake.getBlockMetadataManager().hasMeta(b, "IN_UNIVERSAL_MINE_REGION")) { // If it's in a mining region we don't need to store that its mineable
                UniversalCoreRemake.getBlockMetadataManager().setMeta(b, "IN_UNIVERSAL_MINE_REGION", null);
                UniversalCoreRemake.getBlockMetadataManager().setMeta(b, "MINEABLE", null);
            }
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
        PacketPlayOutAnimation punchAnim = new PacketPlayOutAnimation(((CraftPlayer) p).getHandle(), 0); // I don't know the protocollib id

        breakAnim.getBlockPositionModifier().write(0, new BlockPosition(b.getX(), b.getY(),
                b.getZ()));
        breakAnim.getIntegers().write(0, LocationUtils.getBlockEntityId(b));
        breakAnim.getIntegers().write(1, stage);
        try {
            UniversalCoreRemake.getProtocolManager().sendServerPacket(p, breakAnim);

            for (Player other : Bukkit.getOnlinePlayers()) {
                if (other != p && other.getLocation().distanceSquared(p.getLocation()) <= 11.3137085) { // 128 blocks squared
                    UniversalCoreRemake.getProtocolManager().sendServerPacket(other, breakAnim);
                    ((CraftPlayer) other).getHandle().playerConnection.sendPacket(punchAnim);
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void putRegenningBlock(Block b, int i) {
        regen.put(b, i);
    }

    private float diamondPickMultiplier = 0.75f;
    private float goldPickMultiplier = 0.55f;
    private float ironPickMultiplier = 0.5f;
    private float stonePickMultiplier = 0.4f;
    private float woodPickMultiplier = 0.2f;

    private float getItemMultiplier(Material m) {
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

    enum BreakBehavior {
        DEFAULT, INSTANT_RESPAWN
    }
}