package me.stupidbot.universalcoreremake.managers.universalplayer;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UniversalPlayerManager implements Listener { // TODO Save as .json instead
    public final String dataFolderPath = UniversalCoreRemake.getInstance().getDataFolder() + File.separator +
            "data" + File.separator + "player_data";
    private final List<UniversalPlayer> universalPlayers = Collections.synchronizedList(new ArrayList<>());
    private final Map<UUID, Integer> universalPlayerDictionary = new ConcurrentHashMap<>();
    private long lastRefresh = System.nanoTime();

    public UniversalPlayerManager() {
        initialize();
    }

    public List<UniversalPlayer> getAllUniversalPlayers() {
        return universalPlayers;
    }

    private Map<UUID, Integer> getUniversalPlayerDictionary() {
        return universalPlayerDictionary;
    }

    private void lazilyRefreshCache() {
        if (System.nanoTime() - lastRefresh > 1.8e+12) // 30 minutes, maybe can be longer
            manuallyRefreshCache();
    }

    public void manuallyRefreshCache() {
        Bukkit.getScheduler().runTaskAsynchronously(UniversalCoreRemake.getInstance(), () -> {
            lastRefresh = System.nanoTime();
            List<UniversalPlayer> list = new ArrayList<>();
            Map<UUID, Integer> dictionary = new HashMap<>();

            universalPlayers.forEach((UniversalPlayer up) -> {
                UUID id = up.getUniqueId();
                if (Bukkit.getPlayer(id) != null) {
                    list.add(up);
                    dictionary.put(id, dictionary.size());
                } else
                    up.savePlayerDataFile();
            });

            universalPlayers.clear();
            universalPlayerDictionary.clear();
            universalPlayers.addAll(list);
            universalPlayerDictionary.putAll(dictionary);
        });
    }

    private void createUniversalPlayer(Player p) {
        lazilyRefreshCache();
        File pFileLoc = getPlayerDataFile(p);
        FileConfiguration pFile = loadPlayerDataFile(pFileLoc);

        UniversalPlayer up = new UniversalPlayer(pFile, Objects.requireNonNull(pFileLoc).getPath());

        if (up.firstJoin()) {
            up.setName(p.getName());
            String prefix = UniversalCoreRemake.getChat().getPlayerPrefix(p);
            up.setPrefix(prefix);
            up.setNameColor(prefix.substring(0, 2));
            up.setFirstPlayed(UniversalPlayer.getSimpleDateFormat().format(new Date()));
            try (Stream<Path> files = Files.list(Paths.get(dataFolderPath))) {
                up.setJoinNumber((int) files.count());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<UniversalPlayer> ups = getAllUniversalPlayers();
        getUniversalPlayerDictionary().put(p.getUniqueId(), ups.size());
        ups.add(up);
    }

    private void createUniversalPlayer(OfflinePlayer p) {
        lazilyRefreshCache();
        File pFileLoc = getPlayerDataFile(p);

        if (pFileLoc != null) {
            FileConfiguration pFile = loadPlayerDataFile(pFileLoc);

            UniversalPlayer up = new UniversalPlayer(pFile, pFileLoc.getPath());

            List<UniversalPlayer> ups = getAllUniversalPlayers();
            getUniversalPlayerDictionary().put(p.getUniqueId(), ups.size());
            ups.add(up);
        }
    }

    public UniversalPlayer getUniversalPlayer(Player p) {
        int index;
        UniversalPlayer up;

        if (!getUniversalPlayerDictionary().containsKey(p.getUniqueId()))
            createUniversalPlayer(p);

        index = getUniversalPlayerDictionary().get(p.getUniqueId());

        up = getAllUniversalPlayers().get(index);

        return up;
    }

    public UniversalPlayer getUniversalPlayer(OfflinePlayer p) {
        int index;
        UniversalPlayer up;

        if (!getUniversalPlayerDictionary().containsKey(p.getUniqueId()))
            createUniversalPlayer(p);

        index = getUniversalPlayerDictionary().get(p.getUniqueId());

        up = getAllUniversalPlayers().get(index);

        return up;
    }

    public UniversalPlayer getUniversalPlayer(UUID id) {
        return getUniversalPlayer(Bukkit.getOfflinePlayer(id));
    }

    private File getPlayerDataFile(Player p) {
        File pFile = new File(dataFolderPath + File.separator + p.getUniqueId() + ".yml");
        File pdf = new File(dataFolderPath);

        if (!pdf.exists())
            pdf.mkdirs();
        if (!pFile.exists())
            try {
                pFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        return pFile;
    }

    private File getPlayerDataFile(OfflinePlayer p) {
        File pFile = new File(dataFolderPath + File.separator + p.getUniqueId() + ".yml");

        if (!pFile.exists())
            return null;
        else
            return pFile;
    }

/*    private FileConfiguration loadPlayerDataFile(Player p) {
        File pFile = getPlayerDataFile(p);

        assert pFile != null;
        return YamlConfiguration.loadConfiguration(pFile);
    }*/

    private FileConfiguration loadPlayerDataFile(File f) {
        return YamlConfiguration.loadConfiguration(f);
    }

    private void initialize() {
        Bukkit.getOnlinePlayers().forEach((p) -> {
            createUniversalPlayer(p);
            timePlayed.put(p.getUniqueId(), System.nanoTime());
        });

        Bukkit.getScheduler().runTaskTimerAsynchronously(UniversalCoreRemake.getInstance(), () -> {
            if (System.nanoTime() - lastRefresh > 1.8e+12) { // 30 minutes, maybe can be longer
                lastRefresh = System.nanoTime();
                manuallyRefreshCache();
            }
        }, 1200, 1200);
    }

    public void disable() {
        if (getAllUniversalPlayers() != null)
            if (!getAllUniversalPlayers().isEmpty())
                getAllUniversalPlayers().forEach((UniversalPlayer up) -> {
                    up.setDataLastPlayed(UniversalPlayer.getSimpleDateFormat().format(new Date()));
                    if (timePlayed.containsKey(up.getUniqueId()))
                        up.incrementSecondsPlayed((long) ((System.nanoTime() - timePlayed.get(up.getUniqueId())) / 1e+9));
                    up.savePlayerDataFile();
                });
    }

    public void saveAll() {
        Bukkit.getScheduler().runTaskAsynchronously(UniversalCoreRemake.getInstance(), () ->
                getAllUniversalPlayers().forEach((UniversalPlayer up) -> {
                    up.setDataLastPlayed(UniversalPlayer.getSimpleDateFormat().format(new Date()));
                    up.savePlayerDataFile();
                }));
    }

    private final Map<UUID, Long> timePlayed = new HashMap<>();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        timePlayed.put(p.getUniqueId(), System.nanoTime());
        if (!universalPlayerDictionary.containsKey(p.getUniqueId()))
            Bukkit.getScheduler().runTaskAsynchronously(UniversalCoreRemake.getInstance(), () -> createUniversalPlayer(p));
        else {
            UniversalPlayer up = getUniversalPlayer(p);
            up.setName(p.getName());
            String prefix = UniversalCoreRemake.getChat().getPlayerPrefix(p);
            up.setPrefix(prefix);
            up.setNameColor(prefix.substring(0, 2));
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = getUniversalPlayer(p);

        up.setDataLastPlayed(UniversalPlayer.getSimpleDateFormat().format(new Date()));
        up.incrementSecondsPlayed((long) ((System.nanoTime() - timePlayed.get(p.getUniqueId())) / 1e+9));
        timePlayed.remove(p.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(UniversalCoreRemake.getInstance(), up::savePlayerDataFile);
    }
}