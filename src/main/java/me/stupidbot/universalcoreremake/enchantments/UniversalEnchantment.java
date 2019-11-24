package me.stupidbot.universalcoreremake.enchantments;

import me.stupidbot.universalcoreremake.enchantments.mutation.*;
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
    public static final Enchantment COAL_LOVER = new CoalLover(251);
    public static final Enchantment IRON_LOVER = new IronLover(250);
    public static final Enchantment KILLER_OF_DEATH = new KillerOfDeath(249);
    public static final Enchantment BEING_FIREBALL_IS_SUFFERING = new BeingFireballIsSuffering(248);

    public static final Set<Enchantment> ENCHANTMENTS = new HashSet<>(Arrays.asList(GLASS, SPEED_BOOST, SANDSTONE_LOVER,
            GLOW, COAL_LOVER, IRON_LOVER, KILLER_OF_DEATH, BEING_FIREBALL_IS_SUFFERING));

    public static final Set<Enchantment> MUTATIONS = new HashSet<>(Arrays.asList(GLASS, SPEED_BOOST, SANDSTONE_LOVER,
            COAL_LOVER, IRON_LOVER, KILLER_OF_DEATH, BEING_FIREBALL_IS_SUFFERING));

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
            return "&7Right click for +20% speed for 30 seconds (costs 10 stamina)";
        else if (e == SANDSTONE_LOVER)
            return "&710% mining speed on (red) sandstone and\n&7+10% chance for red sandstone to turn into sandstone";
        else if (e == Enchantment.DIG_SPEED)
            return "&7Mine all blocks +25% faster and +5% for subsequent levels";
        else if (e == COAL_LOVER)
            return "&710% mining speed on coal ore/block and\n&7+10% chance for coal ore to turn into coal block";
        else if (e == IRON_LOVER)
            return "&710% mining speed on iron ore/block and\n&7+10% chance for iron ore to turn into iron block";
        else if (e == KILLER_OF_DEATH)
            return "&740% more damage to (wither) skeletons";
        else if (e == BEING_FIREBALL_IS_SUFFERING)
            return "&7Right click to shoot a fireball (costs 30 stamina)";
        return null;
    }
}