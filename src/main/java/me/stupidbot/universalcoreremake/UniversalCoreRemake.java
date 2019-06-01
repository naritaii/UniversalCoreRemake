package me.stupidbot.universalcoreremake;

import de.slikey.effectlib.EffectManager;
import me.stupidbot.universalcoreremake.Commands.CommandExecutor;
import me.stupidbot.universalcoreremake.Utilities.BlockMetadata;
import me.stupidbot.universalcoreremake.Utilities.Players.UniversalPlayerManager;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class UniversalCoreRemake extends JavaPlugin {
    private static UniversalCoreRemake instance;
    private static EffectManager effectManager;
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    @Override
    public void onEnable() {
        instance = this;
        effectManager = new EffectManager(this);
        CommandExecutor executor = new CommandExecutor();

        registerEvents(instance, new UniversalPlayerManager(), new PlayerLevelling());
        registerCommands(executor, "exp", "setblockmeta");
        setupEconomy();
        setupChat();
        setupPermissions();

        UniversalPlayerManager.onEnable();
        BlockMetadata.onEnable();

        System.out.println(getName() + " is now enabled!");
    }

    @Override
    public void onDisable() {
        UniversalPlayerManager.onDisable();
        BlockMetadata.onDisable();

        System.out.println(getName() + " is now disabled!");
    }

    public static UniversalCoreRemake getInstance() {
        return instance;
    }

    public static EffectManager getEffectManager() {
        return effectManager;
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }
}