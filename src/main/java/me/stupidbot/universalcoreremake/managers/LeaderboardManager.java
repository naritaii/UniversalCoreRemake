package me.stupidbot.universalcoreremake.managers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.LevelUpEvent;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class LeaderboardManager implements Listener {
    private final String folderPath = UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "data";
    private final String dataPath = folderPath + File.separator + "leaderboard_data.json";
    private Map<String, Map<UUID, Double>> sortedData = new ConcurrentHashMap<>();

    public LeaderboardManager() {
        File path = new File(folderPath);
        File file = new File(dataPath);
        if (!path.exists())
            path.mkdirs();
        if (!file.exists()) // Initialize data
            initializeData();
        else // Load data
            loadSortedData();
        instantiateHolograms();
        Bukkit.getOnlinePlayers().forEach(this::updateHolograms);
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValues(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());

        return result;
    }

    private long lastSort;
    private void lazilyUpdateData(String type, UUID id, Double d) {
        Map<UUID, Double> data = sortedData.get(type);
        data.put(id, d);
        sortedData.put(type, data);
        updateHolograms(Bukkit.getPlayer(id));

        if (System.nanoTime() - lastSort > 6e+11) // 10 minutes, can maybe be shorter
            manuallySortData();
    }

    private void manuallySortData() {
        lastSort = System.nanoTime();
        sortedData.forEach((String type, Map<UUID, Double> data) -> sortedData.put(type, sortByValues(data)));
        Bukkit.getOnlinePlayers().forEach(this::updateHolograms);
    }

    private void initializeData() {
        try {
            lastSort = System.nanoTime();
            Map<String, Map<UUID, Double>> playersData = new HashMap<>();

            Files.list(new File(UniversalCoreRemake.getUniversalPlayerManager().dataFolderPath).toPath()).forEach(path -> {
                UUID id = UUID.fromString(FilenameUtils.removeExtension(path.getFileName().toString()));
                UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(id);

                Double money = up.getTotalMoney();
                if (money > 0) {
                    Map<UUID, Double> data = playersData.getOrDefault("TotalMoney", new ConcurrentHashMap<>());
                    data.put(id, money);
                    playersData.put("TotalMoney", data);
                }

                Double level = (double) up.getLevel();
                if (level > 1) {
                    Map<UUID, Double> data = playersData.getOrDefault("XP.Level", new ConcurrentHashMap<>());
                    data.put(id, level);
                    playersData.put("XP.Level", data);
                }
            });
            UniversalCoreRemake.getUniversalPlayerManager().manuallyRefreshCache();

            // Sort data
            playersData.forEach((String type, Map<UUID, Double> data) -> sortedData.put(type, sortByValues(data)));
            saveSortedData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSortedData() {
        try {
            lastSort = System.nanoTime();
            Path dpath = Paths.get(dataPath);
            String str = Files.readAllLines(dpath).get(0);
            Gson gson = new Gson();
            Map<String, Map<UUID, Double>> data = gson.fromJson(str, new TypeToken<Map<String, Map<UUID, Double>>>() {
            }.getType());
            data.forEach((String dataType, Map<UUID, Double> dataMap) -> sortedData.put(dataType, sortByValues(dataMap)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSortedData() {
        try {
            Gson gson = new Gson();
            String str = gson.toJson(sortedData, new TypeToken<Map<String, Map<UUID, Double>>>(){}.getType());
            Path dpath = Paths.get(dataPath);

            if (Files.exists(dpath))
                Files.delete(dpath);
            Files.write(dpath, str.getBytes(), StandardOpenOption.CREATE_NEW);
/*            String read = Files.readAllLines(dpath).get(0);
             assertEquals(str, read);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        saveSortedData();
    }

    private Map<UUID, Hologram> yourPosHolo = new HashMap<>();
    private void updateHolograms(Player player) {

    }

    private void instantiateHolograms() {
        sortedData.forEach((String type, Map<UUID, Double> data) -> {
            
        });
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {

    }

    @EventHandler
    public void OnUserBalanceUpdate(UserBalanceUpdateEvent e) {
        double newMoney = e.getNewBalance().doubleValue(); // Double is fine and FileConfiguration doesn't let me store BigDecimal
        double oldMoney = e.getOldBalance().doubleValue();
        if (newMoney > oldMoney) {
            Player p = e.getPlayer();
            UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
            up.incrementTotalMoney(newMoney - oldMoney);
            lazilyUpdateData("TotalMoney", p.getUniqueId(), up.getTotalMoney());
        }
    }

    @EventHandler
    public void OnLevelUp(LevelUpEvent e) {
        lazilyUpdateData("XP.Level", e.getPlayer().getUniqueId(), (double) e.getLevel());
    }
}
