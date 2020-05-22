package me.stupidbot.universalcoreremake.items;

import com.google.common.collect.ImmutableMap;
import me.stupidbot.universalcoreremake.items.armor.*;
import me.stupidbot.universalcoreremake.items.axes.BasicAxe;
import me.stupidbot.universalcoreremake.items.axes.LumberjacksAxe;
import me.stupidbot.universalcoreremake.items.axes.ReinforcedIronAxe;
import me.stupidbot.universalcoreremake.items.pickaxes.*;
import me.stupidbot.universalcoreremake.items.weapons.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UniversalItem {
    public static final ItemStack RED_SANDSTONE_PICKAXE = new RedSandstonePickaxe();
    public static final ItemStack CORRUPTED_SHARD = new CorruptedShard();
    public static final ItemStack BROKEN_STICK = new BrokenStick();
    public static final ItemStack COAL_ORE_PICKAXE = new CoalOrePickaxe();
    public static final ItemStack IRON_ORE_PICKAXE = new IronOrePickaxe();
    public static final ItemStack CORRUPTED_SWORD = new CorruptedSword();
    public static final ItemStack CORRUPTED_BONE = new CorruptedBone();
    public static final ItemStack BONE_SWORD = new BoneSword();
    public static final ItemStack INFESTED_FIRE = new InfestedFire();
    public static final ItemStack FIRE_WAND = new FireWand();
    private static final ItemStack MR_MEATS_CLEAVER = new MrMeatsCleaver();
    private static final ItemStack MASTER_MINERS_PICKAXE = new MasterMinersPickaxe();
    public static final ItemStack CORRUPTED_PICKAXE = new CorruptPickaxe();
    public static final ItemStack REINFORCED_IRON_AXE = new ReinforcedIronAxe();
    public static final ItemStack REINFORCED_WOODEN_SWORD = new ReinforcedWoodenSword();
    private static final ItemStack BASIC_BOW = new BasicBow();
    public static final ItemStack LEATHER_HELMET = new LeatherHelmet();
    public static final ItemStack LEATHER_CHESTPLATE = new LeatherChestplate();
    public static final ItemStack LEATHER_LEGGINGS = new LeatherLeggings();
    public static final ItemStack LEATHER_BOOTS = new LeatherBoots();
    public static final ItemStack INFESTED_SWORD = new InfestedSword();
    public static final ItemStack LUMBERJACKS_AXE = new LumberjacksAxe();
    public static final ItemStack BASIC_AXE = new BasicAxe();
    public static final ItemStack WOOL_HELMET = new WoolHelmet();
    public static final ItemStack WOOL_CHESTPLATE = new WoolChestplate();
    public static final ItemStack WOOL_LEGGINGS = new WoolLeggings();
    public static final ItemStack WOOL_BOOTS = new WoolBoots();
    public static final ItemStack REINFORCED_IRON_HELMET = new ReinforcedIronHelmet();
    public static final ItemStack REINFORCED_IRON_CHESTPLATE = new ReinforcedIronChestplate();
    public static final ItemStack REINFORCED_IRON_LEGGINGS = new ReinforcedIronLeggings();
    public static final ItemStack REINFORCED_IRON_BOOTS = new ReinforcedIronBoots();
    public static final ItemStack SLIME_HELMET = new SlimeHelmet();
    public static final ItemStack SLIME_CHESTPLATE = new SlimeChestplate();
    public static final ItemStack SLIME_LEGGINGS = new SlimeLeggings();
    public static final ItemStack SLIME_BOOTS = new SlimeBoots();
    public static final ItemStack MINERS_HELMET = new MinersHelmet();
    public static final ItemStack MINERS_CHESTPLATE = new MinersChestplate();
    public static final ItemStack MINERS_LEGGINGS = new MinersLeggings();
    public static final ItemStack MINERS_BOOTS = new MinersBoots();
    public static final ItemStack ARTIFACT_COMMON = new CommonArtifact();

    public static final List<ItemStack> UNIVERSAL_ITEMS = Arrays.asList(RED_SANDSTONE_PICKAXE,
            CORRUPTED_SHARD, BROKEN_STICK, COAL_ORE_PICKAXE, IRON_ORE_PICKAXE, CORRUPTED_SWORD,
            CORRUPTED_BONE, BONE_SWORD, INFESTED_FIRE, FIRE_WAND, MR_MEATS_CLEAVER, MASTER_MINERS_PICKAXE,
            CORRUPTED_PICKAXE, REINFORCED_IRON_AXE, REINFORCED_WOODEN_SWORD, BASIC_BOW, LEATHER_HELMET, LEATHER_CHESTPLATE,
            LEATHER_LEGGINGS, LEATHER_BOOTS, INFESTED_SWORD, LUMBERJACKS_AXE, BASIC_AXE, WOOL_HELMET, WOOL_CHESTPLATE,
            WOOL_LEGGINGS, WOOL_BOOTS, REINFORCED_IRON_HELMET, REINFORCED_IRON_CHESTPLATE, REINFORCED_IRON_LEGGINGS,
            REINFORCED_IRON_BOOTS, SLIME_HELMET, SLIME_CHESTPLATE, SLIME_LEGGINGS, SLIME_BOOTS, MINERS_HELMET,
            MINERS_CHESTPLATE, MINERS_LEGGINGS, MINERS_BOOTS, ARTIFACT_COMMON);
    public static final Map<String, Integer> UNIVERSAL_ITEMS_DICTIONARY = ImmutableMap.<String, Integer>builder()
            .put("RED_SANDSTONE_PICKAXE", 0)
            .put("CORRUPTED_SHARD", 1)
            .put("BROKEN_STICK", 2)
            .put("COAL_ORE_PICKAXE", 3)
            .put("IRON_ORE_PICKAXE", 4)
            .put("CORRUPTED_SWORD", 5)
            .put("CORRUPTED_BONE", 6)
            .put("BONE_SWORD", 7)
            .put("INFESTED_FIRE", 8)
            .put("FIRE_WAND", 9)
            .put("MR_MEATS_CLEAVER", 10)
            .put("MASTER_MINERS_PICKAXE", 11)
            .put("CORRUPTED_PICKAXE", 12)
            .put("REINFORCED_IRON_AXE", 13)
            .put("REINFORCED_WOODEN_SWORD", 14)
            .put("BASIC_BOW", 15)
            .put("LEATHER_HELMET", 16)
            .put("LEATHER_CHESTPLATE", 17)
            .put("LEATHER_LEGGINGS", 18)
            .put("LEATHER_BOOTS", 19)
            .put("INFESTED_SWORD", 20)
            .put("LUMBERJACKS_AXE", 21)
            .put("BASIC_AXE", 22)
            .put("WOOL_HELMET", 23)
            .put("WOOL_CHESTPLATE", 24)
            .put("WOOL_LEGGINGS", 25)
            .put("WOOL_BOOTS", 26)
            .put("REINFORCED_IRON_HELMET", 27)
            .put("REINFORCED_IRON_CHESTPLATE", 28)
            .put("REINFORCED_IRON_LEGGINGS", 29)
            .put("REINFORCED_IRON_BOOTS", 30)
            .put("SLIME_HELMET", 31)
            .put("SLIME_CHESTPLATE", 32)
            .put("SLIME_LEGGINGS", 33)
            .put("SLIME_BOOTS", 34)
            .put("MINERS_HELMET", 35)
            .put("MINERS_CHESTPLATE", 36)
            .put("MINERS_LEGGINGS", 37)
            .put("MINERS_BOOTS", 38)
            .put("ARTIFACT_COMMON", 39)
            .build();

    public UniversalItem(JavaPlugin plugin) {
        try { // Register listeners
            UNIVERSAL_ITEMS.forEach((i) ->
                    Bukkit.getServer().getPluginManager().registerEvents((Listener) i, plugin));
        } catch (Exception ignored) { /* Doesn't have any listeners */ }
    }

    // TODO System to update (parts of) items in players inventory if they're updated in code
}