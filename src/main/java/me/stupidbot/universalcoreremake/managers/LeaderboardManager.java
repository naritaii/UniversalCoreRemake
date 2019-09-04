package me.stupidbot.universalcoreremake.managers;

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
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

class LeaderboardManager implements Listener {
    private final String folderPath = UniversalCoreRemake.getInstance().getDataFolder() + File.separator + "data";
    private final String dataPath = folderPath + File.separator + "leaderboard_data.json";
    private Map<UUID, Map<String, ? extends Comparable>> playersData = new ConcurrentHashMap<>();
    private Map<String, Map<UUID, ? extends Comparable>> sortedData;

    LeaderboardManager() {
        File path = new File(folderPath);
        File file = new File(dataPath);
        if (!path.exists())
            path.mkdirs();
        if (!file.exists()) // Initialize data
            try (Stream<Path> paths = Files.walk(Paths.get(UniversalCoreRemake.getUniversalPlayerManager().dataFolderPath))) {
                paths.forEach((Path ppath) -> {
                    UUID id = UUID.fromString(FilenameUtils.removeExtension(ppath.getFileName().toString()));
                    Map<String, Comparable> data = new ConcurrentHashMap<>();
                    UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(id);

                    data.put("TotalMoney", up.getTotalMoney());
                    data.put("Level", up.getLevel());

                    playersData.put(id, data);
                });
                // TODO Manually refresh UniversalPlayer cache once that method is added
            } catch (IOException e) {
                e.printStackTrace();
            }
        else { // Load data

        }
    }

    private <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = Comparator.comparing(map::get);
        Map<K, V> sortedByValues = new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
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
