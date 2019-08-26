package me.stupidbot.universalcoreremake;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.slikey.effectlib.EffectManager;
import fr.minuskube.inv.InventoryManager;
import me.stupidbot.universalcoreremake.commands.CommandExecutor;
import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.managers.*;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjectiveManager;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.utilities.Stamina;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnusedReturnValue")
public class UniversalCoreRemake extends JavaPlugin {
    private static UniversalCoreRemake instance;
    private static EffectManager effectManager;
    private static ProtocolManager protocolManager;
    private static UniversalPlayerManager universalPlayerManager;
    private static BlockMetadataManger blockMetadataManager;
    private static MiningManager miningManager;
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
    private static InventoryManager inventoryManager = null;
    private static MOTDManager motdManager;
    private static UniversalObjectiveManager universalObjectiveManager;
    private static ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;

        effectManager = new EffectManager(instance);
        protocolManager = ProtocolLibrary.getProtocolManager();
        universalPlayerManager = new UniversalPlayerManager();
        blockMetadataManager = new BlockMetadataManger();
        miningManager = new MiningManager();
        inventoryManager = new InventoryManager(instance);
        motdManager = new MOTDManager();
        universalObjectiveManager = new UniversalObjectiveManager();
        scoreboardManager = new ScoreboardManager();

        CommandExecutor executor = new CommandExecutor();

        setupEconomy();
        setupChat();
        setupPermissions();
        inventoryManager.init();
        new UniversalEnchantment(instance);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new UniversalCoreExpansion().register();
        else
            System.out.println("PlaceholderAPI support disabled.");

        registerEvents(instance, universalPlayerManager, new PlayerLevelling(), miningManager, new Stamina(),
                new ChatManager(), motdManager, new ItemMetadata(), scoreboardManager, universalObjectiveManager);
        registerCommands(executor, "reloadmotd", "reloaduniversalobjectives",
                "exp", "setblockmeta", "readblockmeta", "setitemmeta", "readitemmeta", "emoji",
                "openmineraltrader", "openfoodtrader", "saveuniversalplayercachetofile", "saveblockmetadatacachetofile",
                "selectobjective");

        System.out.println(getName() + " is now enabled!");
    }

    @Override
    public void onDisable() {
        universalObjectiveManager.disable();
        effectManager.dispose();
        universalPlayerManager.disable();
        miningManager.disable();
        blockMetadataManager.disable();

        System.out.println(getName() + " is now disabled!");
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

    public static UniversalCoreRemake getInstance() {
        return instance;
    }

    public static EffectManager getEffectManager() {
        return effectManager;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public static UniversalPlayerManager getUniversalPlayerManager() {
        return universalPlayerManager;
    }

    public static BlockMetadataManger getBlockMetadataManager() {
        return blockMetadataManager;
    }

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public static MOTDManager getMotdManager() {
        return motdManager;
    }

    public static ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public static UniversalObjectiveManager getUniversalObjectiveManager() {
        return universalObjectiveManager;
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