package me.stupidbot.universalcoreremake.managers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.LevelUpEvent;
import me.stupidbot.universalcoreremake.events.UniversalBlockBreakEvent;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    private final Map<String, Map<UUID, Double>> sortedData = new ConcurrentHashMap<>();
    private final Map<String, List<UUID>> sortedPositions = new ConcurrentHashMap<>();

    public LeaderboardManager() {
        File path = new File(folderPath);
        File file = new File(dataPath);
        if (!path.exists())
            //noinspection ResultOfMethodCallIgnored
            path.mkdirs();
        if (!file.exists()) // Initialize data
            initializeData();
        else // Load data
            loadSortedData();

        instantiateHolograms();

        Bukkit.getScheduler().runTaskTimer(UniversalCoreRemake.getInstance(), () -> { // Synchronous because itd break manuallySortData();
            if (System.nanoTime() - lastSort > 9e+11) { // 15 minutes
                lastSort = System.nanoTime();
                manuallySortData();
            }
        }, 1200, 1200);
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

        if (System.nanoTime() - lastSort > 3e+11) // 5 minutes
            manuallySortData();
        else
            updateHolograms(Bukkit.getPlayer(id));
    }

    public void manuallySortData() {
        Bukkit.getScheduler().runTaskAsynchronously(UniversalCoreRemake.getInstance(), () -> {
            lastSort = System.nanoTime();
            sortedData.forEach((String type, Map<UUID, Double> data) -> {
                Map<UUID, Double> sortedMap = sortByValues(data);
                sortedData.put(type, sortedMap);

                List<UUID> orderedList = new ArrayList<>(sortedMap.keySet()); // Creates list from set using iterator which can access position, so its stays sorted
                sortedPositions.put(type, orderedList);
            });
        });
        updateHolograms();
        Bukkit.getOnlinePlayers().forEach(this::updateHolograms);
    }

    public void initializeData() {
        lastSort = System.nanoTime();
        Map<String, Map<UUID, Double>> playersData = new HashMap<>();
        try {
            UniversalCoreRemake.getUniversalPlayerManager().saveAll();
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

                Double blocksMined = (double) up.getBlocksMined();
                if (blocksMined > 0) {
                    Map<UUID, Double> data = playersData.getOrDefault("Stats.BlocksMined", new ConcurrentHashMap<>());
                    data.put(id, blocksMined);
                    playersData.put("Stats.BlocksMined", data);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        UniversalCoreRemake.getUniversalPlayerManager().manuallyRefreshCache();

        // Sort data
        playersData.forEach((String type, Map<UUID, Double> data) -> {
            Map<UUID, Double> sortedMap = sortByValues(data);
            sortedData.put(type, sortedMap);

            List<UUID> orderedList = new ArrayList<>(sortedMap.keySet()); // Creates list from set using iterator which can access position, so its stays sorted
            sortedPositions.put(type, orderedList);
        });
        saveSortedData();
    }

    private void loadSortedData() {
        try {
            lastSort = System.nanoTime();
            Path dpath = Paths.get(dataPath);
            String str = Files.readAllLines(dpath).get(0);
            Gson gson = new Gson();
            Map<String, Map<UUID, Double>> data = gson.fromJson(str, new TypeToken<Map<String, Map<UUID, Double>>>() {
            }.getType());
            // Sort data
            data.forEach((String dataType, Map<UUID, Double> dataMap) -> {
                Map<UUID, Double> sortedMap = sortByValues(dataMap);
                sortedData.put(dataType, sortedMap);

                List<UUID> orderedList = new ArrayList<>(sortedMap.keySet()); // Creates list from set using iterator which can access position, so its stays sorted
                sortedPositions.put(dataType, orderedList);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSortedData() {
        try {
            Gson gson = new Gson();
            String str = gson.toJson(sortedData, new TypeToken<Map<String, Map<UUID, Double>>>() {
            }.getType());
            Path dpath = Paths.get(dataPath);

            if (Files.exists(dpath))
                Files.delete(dpath);
            Files.write(dpath, str.getBytes(), StandardOpenOption.CREATE_NEW);
/*                String read = Files.readAllLines(dpath).get(0);
                Assert.assertEquals(str, read);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        saveSortedData();
    }

    private final Map<String, Location> holoLocs = new ConcurrentHashMap<>();
    private final Map<String, String> displayNames = new ConcurrentHashMap<>();
    private final Map<String, String> formats = new ConcurrentHashMap<>();
    private final Map<String, String> naformats = new ConcurrentHashMap<>();
    private final String configPath = UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "leaderboards.yml";

    public void loadConfig() {
        File path = UniversalCoreRemake.getInstance().getDataFolder();
        File file = new File(configPath);

        if (!path.exists())
            //noinspection ResultOfMethodCallIgnored
            path.mkdirs();
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        sortedData.keySet().forEach((String type) -> {
            if (data.get(type + "." + "World") == null)
                data.set(type + "." + "World", "world");
            if (data.get(type + "." + "X") == null)
                data.set(type + "." + "X", 15205.50);
            if (data.get(type + "." + "Y") == null)
                data.set(type + "." + "Y", 65.00);
            if (data.get(type + "." + "Z") == null)
                data.set(type + "." + "Z", 10103.50);
            if (data.get(type + "." + "DisplayName") == null)
                data.set(type + "." + "DisplayName", "&b&l" + type);
            if (data.get(type + "." + "Format") == null)
                data.set(type + "." + "Format", "&e%pos%. %player%&7 - &e%integer%"); // Can also be %double%
            if (data.get(type + "." + "NotAvailable") == null)
                data.set(type + "." + "NotAvailable", "%player%&7 - &cN/A");

            holoLocs.put(type, new Location(Bukkit.getWorld(data.getString(type + "." + "World").trim()),
                    data.getDouble(type + "." + "X"), data.getDouble(type + "." + "Y"),
                    data.getDouble(type + "." + "Z")));
            displayNames.put(type, data.getString(type + "." + "DisplayName").trim());
            formats.put(type, data.getString(type + "." + "Format").trim());
            naformats.put(type, data.getString(type + "." + "NotAvailable").trim());
        });

        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final Map<String, Hologram> holos = new ConcurrentHashMap<>();

    private void instantiateHolograms() {
        loadConfig();
        sortedData.keySet().forEach((String type) ->
                holos.put(type, HologramsAPI.createHologram(UniversalCoreRemake.getInstance(), holoLocs.get(type))));
        updateHolograms();
        Bukkit.getOnlinePlayers().forEach(this::updateHolograms);
    }

    private void updateHolograms() {
        sortedData.forEach((String type, Map<UUID, Double> data) -> {
            Hologram h = holos.get(type);
            h.clearLines();
            List<UUID> poss = sortedPositions.get(type);

            for (int i = 0; i < 11; i++)
                if (i < 1) { // i == 0
                    h.appendTextLine(ChatColor.translateAlternateColorCodes('&', displayNames.get(type)));
                    h.appendTextLine("");
                } else if (!(poss.size() < i)) {
                    int pos = i - 1;
                    UUID id = poss.get(pos);
                    UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(id);
                    h.appendTextLine(ChatColor.translateAlternateColorCodes('&', formats.get(type)
                            .replace("%pos%", ++pos + "")
                            .replace("%player%", up.getNameColor() + up.getName())
                            .replace("%integer%", TextUtils.addCommas(data.get(id).intValue()))
                            .replace("%leveltag%", PlayerLevelling.levelTag(data.get(id).intValue()))
                            .replace("%double%", TextUtils.addCommas(data.get(id)))));
                } else
                    h.appendTextLine("");
        });
    }

    private final Map<UUID, Map<String, Hologram>> yourPosHolos = new ConcurrentHashMap<>();

    private void updateHolograms(Player p) {
        UUID id = p.getUniqueId();
        Map<String, Hologram> holos;
        if (!yourPosHolos.containsKey(id)) {
            holos = new HashMap<>();
            sortedData.keySet().forEach((String type) -> {
                Location loc = holoLocs.get(type).clone().add(0d, -3.5, 0d);
                Hologram h = HologramsAPI.createHologram(UniversalCoreRemake.getInstance(), loc);
                VisibilityManager vm = h.getVisibilityManager();
                vm.showTo(p);
                vm.setVisibleByDefault(false);

                holos.put(type, h);
            });
            yourPosHolos.put(id, holos);
        } else
            holos = yourPosHolos.get(id);

        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        sortedData.forEach((String type, Map<UUID, Double> data) -> {
            Hologram h = holos.get(type);
            h.clearLines();
            if (data.containsKey(id) && sortedPositions.get(type).contains(id)) {
                h.appendTextLine(ChatColor.translateAlternateColorCodes('&', formats.get(type)
                        .replace("%pos%", TextUtils.addCommas(sortedPositions.get(type).indexOf(id) + 1))
                        .replace("%player%", up.getNameColor() + ChatColor.BOLD + up.getName())
                        .replace("%integer%", TextUtils.addCommas(data.get(id).intValue()))
                        .replace("%leveltag%", PlayerLevelling.levelTag(data.get(id).intValue()))
                        .replace("%double%", TextUtils.addCommas(data.get(id)))));
            } else
                h.appendTextLine(ChatColor.translateAlternateColorCodes('&', naformats.get(type)
                        .replace("%player%", up.getNameColor() + ChatColor.BOLD + up.getName())));
        });
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        updateHolograms(e.getPlayer());
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        yourPosHolos.get(id).values().forEach(Hologram::delete);
        yourPosHolos.remove(id);
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

    @EventHandler
    public void OnBlockMined(UniversalBlockBreakEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        lazilyUpdateData("Stats.BlocksMined", id,
                sortedData.get("Stats.BlocksMined").getOrDefault(id, 0d) + 1);
    }
}
