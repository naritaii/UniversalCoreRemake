package me.stupidbot.universalcoreremake;

import org.bukkit.plugin.java.JavaPlugin;

public class UniversalCoreRemake extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("onEnable called!");
    }

    @Override
    public void onDisable() {

        getLogger().info("onDisable called!");
    }
}
