package me.stupidbot.universalcoreremake.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class LeaderboardManager implements Listener {
    private final String folderPath = UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "data";
    private final String dataPath = folderPath + File.separator + "leaderboard_data.json";
    private Map<String, Map<UUID, ? extends Comparable>> sortedData = new ConcurrentHashMap<>();

    public LeaderboardManager() {
        File path = new File(folderPath);
        File file = new File(dataPath);
        if (!path.exists())
            path.mkdirs();
        if (!file.exists()) // Initialize data
            initializeData();
        else // Load data
            loadSortedData();
    }

    private <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = Comparator.comparing(map::get);
        Map<K, V> sortedByValues = new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

    private void initializeData() {
        try (Stream<Path> paths = Files.walk(Paths.get(UniversalCoreRemake.getUniversalPlayerManager().dataFolderPath))) {
            Map<String, Map<UUID, ? extends Comparable>> playersData = new HashMap<>();

            paths.filter(Files::isRegularFile).forEach((Path path) -> {
                UUID id = UUID.fromString(FilenameUtils.removeExtension(path.getFileName().toString()));
                UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(id);

                Double money = up.getTotalMoney();
                if (money > 0) {
                    Map<UUID, Comparable> data = new HashMap<>();
                    data.put(id, money);
                    playersData.put("TotalMoney", data);
                }

                Integer level = up.getLevel();
                if (level > 1) {
                    Map<UUID, Comparable> data = new HashMap<>();
                    data.put(id, money);
                    playersData.put("XP.Level", data);
                }
            }); // TODO Manually refresh UniversalPlayer cache once that method is added or just remove players that aren't online or just never cache the files

            // Sort data
            playersData.forEach((String type, Map<UUID, ? extends Comparable> data) -> sortedData.put(type, sortByValues(data)));

            saveSortedData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSortedData() {
        try {
            Path dpath = Paths.get(dataPath);
            String str = Files.readAllLines(dpath).get(0);
            Gson gson = new Gson();
            Map<String, Map<UUID, ? extends Comparable>> data = gson.fromJson(str, new TypeToken<Map<String, Map<UUID, ? extends Comparable>>>() {
            }.getType());
            data.forEach((String dataType, Map<UUID, ? extends Comparable> dataMap) -> sortedData.put(dataType, sortByValues(dataMap)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSortedData() {
        try {
            Gson gson = new Gson();
            String str = gson.toJson(sortedData);
            Path dpath = Paths.get(dataPath);

            if (Files.exists(dpath))
                Files.delete(dpath);
            Files.write(dpath, str.getBytes(), StandardOpenOption.CREATE_NEW);
            String read = Files.readAllLines(dpath).get(0);
            // assertEquals(str, read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        saveSortedData();
    }

    @EventHandler
    public void OnUserBalanceUpdate(UserBalanceUpdateEvent e) {
        double newMoney = e.getNewBalance().doubleValue(); // Double is fine and FileConfiguration doesn't let me store BigDecimal
        double oldMoney = e.getOldBalance().doubleValue();
        if (newMoney > oldMoney)
            UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(e.getPlayer()).incrementTotalMoney(newMoney - oldMoney);
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {

    }
}
