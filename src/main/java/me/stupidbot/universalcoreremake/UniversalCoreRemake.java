package me.stupidbot.universalcoreremake;

import me.stupidbot.universalcoreremake.Cosmetic.Trail.Trail;
import me.stupidbot.universalcoreremake.Players.UniversalPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class UniversalCoreRemake extends JavaPlugin {

    private static UniversalCoreRemake instance;

    @Override
    public void onEnable() {
        instance = this;

        registerEvents(instance, new Trail(), new UniversalPlayerManager());

        System.out.println(getName() + " is now enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println(getName() + " is now disabled!");
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