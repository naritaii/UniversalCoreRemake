package me.stupidbot.universalcoreremake.Enchantments;

import me.stupidbot.universalcoreremake.Enchantments.Mutations.Glass;
import me.stupidbot.universalcoreremake.Events.UniversalBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UniversalEnchantment {
    public static Enchantment GLASS = new Glass(255);

    public static List<Enchantment> ENCHANTMENTS = new ArrayList<>();
    static {
        ENCHANTMENTS.add(GLASS);
    }

    public UniversalEnchantment(JavaPlugin plugin) {
        registerEnchantments();
        Bukkit.getServer().getPluginManager().registerEvents((Listener) GLASS, plugin);
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
                byId.remove(ench.getId());
                byName.remove(ench.getName());

            });
        } catch (Exception ignored) { }

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

    @EventHandler
    public void OnUniversalBlockBreak(UniversalBlockBreakEvent e) {
        Player p = e.getPlayer();
        
    }
}