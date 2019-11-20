package me.stupidbot.universalcoreremake.items;

import com.google.common.collect.ImmutableMap;
import me.stupidbot.universalcoreremake.items.pickaxes.CoalOrePickaxe;
import me.stupidbot.universalcoreremake.items.pickaxes.IronOrePickaxe;
import me.stupidbot.universalcoreremake.items.pickaxes.RedSandstonePickaxe;
import me.stupidbot.universalcoreremake.items.weapons.BoneSword;
import me.stupidbot.universalcoreremake.items.weapons.BrokenStick;
import me.stupidbot.universalcoreremake.items.weapons.CorruptedSword;
import me.stupidbot.universalcoreremake.items.weapons.FireWand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UniversalItem {
    public static ItemStack RED_SANDSTONE_PICKAXE = new RedSandstonePickaxe();
    public static ItemStack CORRUPTED_SHARD = new CorruptedShard();
    public static ItemStack BROKEN_STICK = new BrokenStick();
    public static ItemStack COAL_ORE_PICKAXE = new CoalOrePickaxe();
    public static ItemStack IRON_ORE_PICKAXE = new IronOrePickaxe();
    public static ItemStack CORRUPTED_SWORD = new CorruptedSword();
    public static ItemStack CORRUPTED_BONE = new CorruptedBone();
    public static ItemStack BONE_SWORD = new BoneSword();
    public static ItemStack INFESTED_FIRE = new InfestedFire();
    public static ItemStack FIRE_WAND = new FireWand();

    public static final List<ItemStack> UNIVERSAL_ITEMS = Arrays.asList(RED_SANDSTONE_PICKAXE,
            CORRUPTED_SHARD, BROKEN_STICK, COAL_ORE_PICKAXE, IRON_ORE_PICKAXE, CORRUPTED_SWORD,
            CORRUPTED_BONE, BONE_SWORD, INFESTED_FIRE, FIRE_WAND);
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
            .build();

    public UniversalItem(JavaPlugin plugin) {
        try { // Register listeners
            UNIVERSAL_ITEMS.forEach((i) ->
                    Bukkit.getServer().getPluginManager().registerEvents((Listener) i, plugin));
        } catch (Exception ignored) { /* Doesn't have any listeners */ }
    }

    // TODO System to update (parts of) items in players inventory if they're updated.
}