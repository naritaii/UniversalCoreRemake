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

    private static List<UniversalPlayer> universalPlayers = new ArrayList<UniversalPlayer>();
    private static HashMap<UUID, Integer> universalPlayerDictionary = new HashMap<UUID, Integer>();

    public static UniversalPlayer getUniversalPlayer(Player p) {
        int index = universalPlayerDictionary.get(p.getUniqueId());
        UniversalPlayer up = universalPlayers.get(index);

        if (up == null)
            up = createUniversalPlayer(p);

        return up;
    }

    public static List<UniversalPlayer> getOnlineUniversalPlayers() {
        return universalPlayers;
    }

    public static UniversalPlayer createUniversalPlayer(Player p) {
        File pFileLoc = getPlayerDataFile(p);
        FileConfiguration pFile = loadPlayerDataFile(pFileLoc);

        UniversalPlayer up = new UniversalPlayer(pFileLoc, pFile);


        up.setPlayerDataName(p.getName());
        if (up.getPlayerDataFirstPlayed() == null)
            up.setPlayerDataFirstPlayed(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));


        universalPlayerDictionary.put(p.getUniqueId(), universalPlayers.size());
        universalPlayers.add(up);

        return up;
    }

    private static File getPlayerDataFile(Player p) {
        File pFile = new File(
                UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "Player Data" + File.separator +
                        p.getUniqueId() + ".yml");
        File pdf = new File(UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "Player Data");

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

        return YamlConfiguration.loadConfiguration(pFile);
    }

    private static FileConfiguration loadPlayerDataFile(File f) {
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