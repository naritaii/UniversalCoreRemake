package me.stupidbot.universalcoreremake;

import me.stupidbot.universalcoreremake.Cosmetic.Trail.Trail;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class UniversalCoreRemake extends JavaPlugin {
    private static UniversalCoreRemake instance;

    @Override
    public void onEnable() {
        instance = this;

        registerEvents(instance, new Trail());

        getLogger().info("onEnable called!");
    }

    @Override
    public void onDisable() {

        getLogger().info("onDisable called!");
    }

    public static UniversalCoreRemake getInstance() {
        return instance;
    }

    private static void registerEvents(Plugin plugin, Listener... listeners) {
        Listener[] arrayOfListener;
        int j = (arrayOfListener = listeners).length;
        for (int i = 0; i < j; i++) {
            Listener listener = arrayOfListener[i];
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}