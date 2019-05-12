package me.stupidbot.universalcoreremake;

import me.stupidbot.universalcoreremake.Commands.CommandExecutor;
import me.stupidbot.universalcoreremake.Cosmetic.Trail.TrailManager;
import me.stupidbot.universalcoreremake.Utilities.BlockMetadata;
import me.stupidbot.universalcoreremake.Utilities.Players.UniversalPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class UniversalCoreRemake extends JavaPlugin {
    private static UniversalCoreRemake instance;

    @Override
    public void onEnable() {
        instance = this;
        CommandExecutor executor = new CommandExecutor();

        registerEvents(instance, new TrailManager(), new UniversalPlayerManager());
        registerCommands(executor, "settrail", "setblockmeta");

        // MySQL.connect();
        UniversalPlayerManager.onEnable();
        BlockMetadata.onEnable();
        TrailManager.onEnable();

        System.out.println(getName() + " is now enabled!");
    }

    @Override
    public void onDisable() {
        UniversalPlayerManager.onDisable();
        BlockMetadata.onDisable();
        // MySQL.disconnect();

        System.out.println(getName() + " is now disabled!");
    }

    public static UniversalCoreRemake getInstance() {
        return instance;
    }

    private void registerEvents(Plugin plugin, Listener... listeners) {
        Listener[] arrayOfListeners;
        int j = (arrayOfListeners = listeners).length;

        for (int i = 0; i < j; i++) {
            Listener listener = arrayOfListeners[i];
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    private void registerCommands(CommandExecutor executor, String... commands) {
        String[] arrayOfCommands;
        int j = (arrayOfCommands = commands).length;

        for (int i = 0; i < j; i++) {
            String command = arrayOfCommands[i];
            getCommand(command).setExecutor(executor);
        }
    }
}