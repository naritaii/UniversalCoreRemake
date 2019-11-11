package me.stupidbot.universalcoreremake.items;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UniversalItem {
    public static RedSandstonePickaxe RED_SANDSTONE_PICKAXE = new RedSandstonePickaxe();
    public static CorruptedShard CORRUPTED_SHARD = new CorruptedShard();
    public static BrokenStick BROKEN_STICK = new BrokenStick();

    public static final Set<ItemStack> UNIVERSAL_ITEMS = new HashSet<>(Arrays.asList(RED_SANDSTONE_PICKAXE,
            CORRUPTED_SHARD, BROKEN_STICK));

    public UniversalItem(JavaPlugin plugin) {
        try { // Register listeners
            UNIVERSAL_ITEMS.forEach((i) -> Bukkit.getServer().getPluginManager().registerEvents((Listener) i, plugin));
        } catch (Exception ignored) { /* Doesn't have any listeners */ }
    }
}