package me.stupidbot.universalcoreremake.enchantments;

import me.stupidbot.universalcoreremake.enchantments.mutation.Glass;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UniversalEnchantment {
    private static final Enchantment GLASS = new Glass(255);

    private static final List<Enchantment> ENCHANTMENTS = Arrays.asList(GLASS);

    public static List<Enchantment> MUTATIONS = Arrays.asList(GLASS);

    public UniversalEnchantment(JavaPlugin plugin) {
        registerEnchantments();
        try { // Register listeners
            ENCHANTMENTS.forEach((ench) -> Bukkit.getServer().getPluginManager().registerEvents((Listener) ench, plugin));
        } catch (Exception ignored) { /* Doesn't have any listeners */ }
    }

    private void registerEnchantments() {
        // Unregister formerly registered enchants
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");

            byIdField.setAccessible(true);
            byNameField.setAccessible(true);

            @SuppressWarnings("unchecked")
            HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(null);

            ENCHANTMENTS.forEach((ench) -> {
                //noinspection deprecation
                byId.remove(ench.getId());
                byName.remove(ench.getName());

            });
        } catch (Exception ignored) { /* Wasn't registered */ }

        // Register enchants
        try {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ENCHANTMENTS.forEach(Enchantment::registerEnchantment);
            } catch (IllegalArgumentException e) {
                e.printStackTrace(); // If this is thrown it probably means the id is already taken
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}