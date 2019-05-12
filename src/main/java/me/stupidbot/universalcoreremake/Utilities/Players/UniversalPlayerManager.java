package me.stupidbot.universalcoreremake.Utilities.Players;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UniversalPlayerManager implements Listener {
    private static final String dataFolderPath = UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "player_data";

    private static List<UniversalPlayer> universalPlayers = new ArrayList<UniversalPlayer>();
    private static HashMap<UUID, Integer> universalPlayerDictionary = new HashMap<UUID, Integer>();

    private static List<UniversalPlayer> getOnlineUniversalPlayers() {
        return universalPlayers;
    }

    private static HashMap<UUID, Integer> getUniversalPlayerDictionary() {
        return universalPlayerDictionary;
    }

    private static UniversalPlayer createUniversalPlayer(Player p) {
        File pFileLoc = getPlayerDataFile(p);
        FileConfiguration pFile = loadPlayerDataFile(pFileLoc);

        UniversalPlayer up = new UniversalPlayer(pFileLoc, pFile);


        up.setPlayerDataName(p.getName());
        if (up.getPlayerDataFirstPlayed() == null)
            up.setPlayerDataFirstPlayed(new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss").format(new Date()));

        List<UniversalPlayer> ups = getOnlineUniversalPlayers();
        getUniversalPlayerDictionary().put(p.getUniqueId(), ups.size());
        ups.add(up);

        return up;
    }

    public static UniversalPlayer getUniversalPlayer(Player p) {
        Integer index = getUniversalPlayerDictionary().get(p.getUniqueId());
        UniversalPlayer up;

        if (index != null)
            up = getOnlineUniversalPlayers().get(index);
        else
            up = createUniversalPlayer(p);

        return up;
    }

    private static File getPlayerDataFile(Player p) {
        File pFile = new File(dataFolderPath + File.separator + p.getUniqueId() + ".yml");
        File pdf = new File(dataFolderPath);

        if (!pdf.exists())
            pdf.mkdirs();
        if (!pFile.exists())
            try {
                pFile.createNewFile();
            } catch (IOException e) { // TODO Properly handle errors
                e.printStackTrace();
                return null;
            }

        return pFile;
    }

    private static FileConfiguration loadPlayerDataFile(Player p) {
        File pFile = getPlayerDataFile(p);

        assert pFile != null;
        return YamlConfiguration.loadConfiguration(pFile);
    }

    private static FileConfiguration loadPlayerDataFile(File f) {
        return YamlConfiguration.loadConfiguration(f);
    }

    public static void onEnable() {
        for (Player all : Bukkit.getOnlinePlayers())
            UniversalPlayerManager.createUniversalPlayer(all);
    }

    public static void onDisable() {
        for (UniversalPlayer all : UniversalPlayerManager.getOnlineUniversalPlayers()) {
            all.setPlayerDataLastPlayed(new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss").format(new Date()));
            all.savePlayerDataFile();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        createUniversalPlayer(p);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = getUniversalPlayer(p);

        up.setPlayerDataLastPlayed(new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss").format(new Date()));


        up.savePlayerDataFile();
    }
}