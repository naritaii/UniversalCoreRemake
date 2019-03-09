package me.stupidbot.universalcoreremake.Players;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
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
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.*;

public class UniversalPlayerManager implements Listener {

    private List<UniversalPlayer> universalPlayers;
    private Dictionary<UUID, Integer> universalPlayerDictionary;

    public UniversalPlayer getUniversalPlayer(Player p) {
        int index = universalPlayerDictionary.get(p.getUniqueId());
        UniversalPlayer uP = universalPlayers.get(index);

        if (uP == null)
            uP = createUniversalPlayer(p);

        return uP;
    }

    private UniversalPlayer createUniversalPlayer(Player p) {
        File pFileLoc = getPlayerDataFile(p);
        FileConfiguration pFile = loadPlayerDataFile(pFileLoc);

        UniversalPlayer uP = new UniversalPlayer(pFileLoc, pFile);


        uP.setPlayerDataName(p.getName());
        if (uP.getPlayerDataFirstPlayed() == null)
            uP.setPlayerDataFirstPlayed(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));


        universalPlayerDictionary.put(p.getUniqueId(), universalPlayers.size());
        universalPlayers.add(uP);

        return uP;
    }

    private File getPlayerDataFile(Player p) {
        return new File(
                UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "Player Data" + File.separator +
                        p.getUniqueId() + ".yml");
    }

    private FileConfiguration loadPlayerDataFile(Player p) {
        File pFile = getPlayerDataFile(p);

        if (!pFile.exists())
            try {
                pFile.createNewFile();
            } catch (IOException e) { // TODO Properly handle errors
                e.printStackTrace();
                return null;
            }

        return YamlConfiguration.loadConfiguration(pFile);
    }

    private FileConfiguration loadPlayerDataFile(File f) {
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) { // TODO Properly handle errors
                e.printStackTrace();
                return null;
            }

        return YamlConfiguration.loadConfiguration(f);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        createUniversalPlayer(p);
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer uP = getUniversalPlayer(p);

        uP.setPlayerDataLastPlayed(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));

        uP.savePlayerDataFile();
    }
}