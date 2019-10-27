package me.stupidbot.universalcoreremake;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import de.slikey.effectlib.EffectManager;
import fr.minuskube.inv.InventoryManager;
import me.stupidbot.universalcoreremake.commands.CommandExecutor;
import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.listeners.CollectibleSlimesListener;
import me.stupidbot.universalcoreremake.listeners.EnderchestListener;
import me.stupidbot.universalcoreremake.listeners.SpawnPortalListener;
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
    private static LeaderboardManager leaderboardManager;
    private static RewardManager rewardManager;
    private static WorldGuardPlugin worldGuardPlugin;

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
        leaderboardManager = new LeaderboardManager();
        rewardManager = new RewardManager();

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
                new ChatManager(), motdManager, new ItemMetadata(), universalObjectiveManager, scoreboardManager,
                new CollectibleSlimesListener(), leaderboardManager, new EnderchestListener(), new SpawnPortalListener(),
                rewardManager);
        registerCommands(executor, "reloadmotd", "reloaduniversalobjectives",
                "exp", "setblockmeta", "readblockmeta", "setitemmeta", "readitemmeta", "emoji",
                "openmineraltrader", "openfoodtrader", "openstats", "openspawnportal", "openquestmaster",
                "saveuniversalplayercachetofile", "saveblockmetadatacachetofile", "selectobjective", "mutate",
                "serialize", "stringreward");

        System.out.println(getName() + " is now enabled!");
    }

    // declare your flag as a field accessible to other parts of your code (so you can use this to check it)
    // note: if you want to use a different type of flag, make sure you change StateFlag here and below to that type
    public static SetFlag UNIVERSAL_MINE;
    public static StringFlag UNIVERSAL_REGION_NAME;

    @Override
    public void onLoad() {
        // ... do your own plugin things, etc
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardPlugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
            FlagRegistry registry = worldGuardPlugin.getFlagRegistry();

            try {
                // create a flag with the name "my-custom-flag", defaulting to true
                SetFlag flag = new SetFlag<>("universal-mine", new StringFlag(null));
                registry.register(flag);
                UNIVERSAL_MINE = flag; // only set our field if there was no error
            } catch (FlagConflictException e) {
                // some other plugin registered a flag by the same name already.
                // you can use the existing flag, but this may cause conflicts - be sure to check type
                Flag<?> existing = registry.get("universal-mine");
                if (existing instanceof SetFlag)
                    UNIVERSAL_MINE = (SetFlag) existing;
                else
                    // types don't match - this is bad news! some other plugin conflicts with you
                    // hopefully this never actually happens
                    System.out.println("Could not initialize universal-mine WorldGuard flag.");
            }

            try {
                StringFlag flag = new StringFlag("universal-region-name");
                registry.register(flag);
                UNIVERSAL_REGION_NAME = flag;
            } catch (FlagConflictException e) {
                Flag<?> existing = registry.get("universal-region-name");
                if (existing instanceof StringFlag)
                    UNIVERSAL_REGION_NAME = (StringFlag) existing;
                else
                    System.out.println("Could not initialize universal-region-name WorldGuard flag.");
            }
        } else
            System.out.println("WorldGuard support disabled.");
    }

    @Override
    public void onDisable() {
        universalObjectiveManager.disable();
        effectManager.dispose();
        universalPlayerManager.disable();
        miningManager.disable();
        blockMetadataManager.disable();
        leaderboardManager.disable();

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

    public static LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public static RewardManager getRewardManager() {
        return rewardManager;
    }

    public static WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
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