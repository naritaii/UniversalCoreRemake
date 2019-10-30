package me.stupidbot.universalcoreremake.enchantments;

import me.stupidbot.universalcoreremake.enchantments.mutation.Glass;
import me.stupidbot.universalcoreremake.enchantments.mutation.SandstoneLover;
import me.stupidbot.universalcoreremake.enchantments.mutation.SpeedBoost;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UniversalEnchantment {
    private static final Enchantment GLASS = new Glass(255);
    private static final Enchantment SPEED_BOOST = new SpeedBoost(254);
    public static final Enchantment SANDSTONE_LOVER = new SandstoneLover(253);
    public static final Enchantment GLOW = new Glow(252);

    public static final Set<Enchantment> ENCHANTMENTS = new HashSet<>(Arrays.asList(GLASS, SPEED_BOOST, SANDSTONE_LOVER,
            GLOW));

    public static final Set<Enchantment> MUTATIONS = new HashSet<>(Arrays.asList(GLASS, SPEED_BOOST, SANDSTONE_LOVER));

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

    public static String getDescription(Enchantment e) {
        if (e == GLASS)
            return "&7Chance item may break when used";
        else if (e == SPEED_BOOST)
            return "&7Right click for +20% speed for 30 seconds (costs 25 stamina)";
        else if (e == SANDSTONE_LOVER)
            return "&75% mining speed on (red) sandstone and\n&7+5% chance for red sandstone to turn into sandstone";
        else if (e == Enchantment.DIG_SPEED)
            return "&7Mine all blocks +25% faster and 5% for subsequent level";
        return null;
    }
}