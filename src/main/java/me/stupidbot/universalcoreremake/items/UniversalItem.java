package me.stupidbot.universalcoreremake.items;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class UniversalItem {
    public static RedSandstonePickaxe RED_SANDSTONE_PICKAXE = new RedSandstonePickaxe();
    public static CorruptedShard CORRUPTED_SHARD = new CorruptedShard();
    public static BrokenStick BROKEN_STICK = new BrokenStick();
    public static CoalOrePickaxe COAL_ORE_PICKAXE = new CoalOrePickaxe();
    public static IronOrePickaxe IRON_ORE_PICKAXE  = new IronOrePickaxe();

    public static final List<ItemStack> UNIVERSAL_ITEMS = Arrays.asList(RED_SANDSTONE_PICKAXE,
            CORRUPTED_SHARD, BROKEN_STICK, COAL_ORE_PICKAXE, IRON_ORE_PICKAXE);
    public static final Map<String, Integer> UNIVERSAL_ITEMS_DICTIONARY = ImmutableMap.<String, Integer>builder()
            .put("RED_SANDSTONE_PICKAXE", 0)
            .put("CORRUPTED_SHARD", 1)
            .put("BROKEN_STICK", 2)
            .put("COAL_ORE_PICKAXE", 3)
            .put("IRON_ORE_PICKAXE", 4)
            .build();

    public UniversalItem(JavaPlugin plugin) {
        try { // Register listeners
            UNIVERSAL_ITEMS.forEach((i) ->
                Bukkit.getServer().getPluginManager().registerEvents((Listener) i, plugin));
        } catch (Exception ignored) { /* Doesn't have any listeners */ }
    }
}