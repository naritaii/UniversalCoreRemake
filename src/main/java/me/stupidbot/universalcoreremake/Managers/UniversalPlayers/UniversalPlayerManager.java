package me.stupidbot.universalcoreremake.Managers.UniversalPlayers;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.Stamina;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UniversalPlayerManager implements Listener {
    private final String dataFolderPath = UniversalCoreRemake.getInstance().getDataFolder() + File.separator +
            "data" + File.separator + "player_data";
    private final List<UniversalPlayer> universalPlayers = new ArrayList<>();
    private final HashMap<UUID, Integer> universalPlayerDictionary = new HashMap<>();

    private List<UniversalPlayer> getAllUniversalPlayers() {
        return universalPlayers;
    }

    private HashMap<UUID, Integer> getUniversalPlayerDictionary() {
        return universalPlayerDictionary;
    }

    private UniversalPlayer createUniversalPlayer(Player p) {
        File pFileLoc = getPlayerDataFile(p);
        FileConfiguration pFile = loadPlayerDataFile(pFileLoc);

        UniversalPlayer up = new UniversalPlayer(pFileLoc, pFile);


        up.setName(p.getName());
        String prefix = UniversalCoreRemake.getChat().getPlayerPrefix(p);
        up.setPrefix(prefix);
        up.setNameColor(prefix.substring(0, 2));

        if (up.firstJoin()) {
            up.setFirstPlayed(UniversalPlayer.getSimpleDateFormat().format(new Date()));
            up.setLevel(1);
            up.setStamina(Stamina.getMaxStamina(1));
        } else // Editing UniversalPlayers in PlayerJoinEvent is glitchy so we handle it here if needed.
            try {
                if (new Date().getTime() - UniversalPlayer.getSimpleDateFormat().parse(up.getLastPlayed())
                        .getTime() >= 6.48e+7) { // 18 hours // TODO Move this
                    up.setStamina(Stamina.getMaxStamina(up.getLevel()));
                    p.setFoodLevel(20);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&a&lALL STAMINA RECOVERED!&7&o All stamina is recovered when you've been " +
                                    "offline for 18+ hours."));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        List<UniversalPlayer> ups = getAllUniversalPlayers();
        getUniversalPlayerDictionary().put(p.getUniqueId(), ups.size());
        ups.add(up);

        return up;
    }

    private UniversalPlayer createUniversalPlayer(OfflinePlayer p) {
        File pFileLoc = getPlayerDataFile(p);

        if (pFileLoc != null) {
            FileConfiguration pFile = loadPlayerDataFile(pFileLoc);

            UniversalPlayer up = new UniversalPlayer(pFileLoc, pFile);

            List<UniversalPlayer> ups = getAllUniversalPlayers();
            getUniversalPlayerDictionary().put(p.getUniqueId(), ups.size());
            ups.add(up);

            return up;
        } else
            return null;
    }

    public UniversalPlayer getUniversalPlayer(Player p) {
        Integer index = getUniversalPlayerDictionary().get(p.getUniqueId());
        UniversalPlayer up;

        if (index != null)
            up = getAllUniversalPlayers().get(index);
        else
            up = createUniversalPlayer(p);

        return up;
    }

    public UniversalPlayer getUniversalPlayer(OfflinePlayer p) {
        Integer index = getUniversalPlayerDictionary().get(p.getUniqueId());
        UniversalPlayer up;

        if (index != null)
            up = getAllUniversalPlayers().get(index);
        else
            up = createUniversalPlayer(p);

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

    private FileConfiguration loadPlayerDataFile(Player p) {
        File pFile = getPlayerDataFile(p);

        assert pFile != null;
        return YamlConfiguration.loadConfiguration(pFile);
    }

    private FileConfiguration loadPlayerDataFile(File f) {
        return YamlConfiguration.loadConfiguration(f);
    }

    public void initialize() {
        Bukkit.getOnlinePlayers().forEach((Consumer<Player>) this::createUniversalPlayer);

//        Bukkit.getScheduler().runTaskTimerAsynchronously(UniversalCoreRemake.getInstance(), () -> {
//            long startTime = System.nanoTime();
//
//            getAllUniversalPlayers().forEach(UniversalPlayer::savePlayerDataFile);
//
//            long endTime = System.nanoTime();
//            String s = ChatColor.translateAlternateColorCodes('&',
//                    "&c[&fDEBUG&c]: &cSaved all cached UniversalPlayer data to file &a(took " +
//                            TextUtils.addCommas((int) ((endTime - startTime) / 1000000)) + "ms)");
//
//            Bukkit.broadcast(s, "universalcore.admin");
//            System.out.println(s);
//        }, (Duration.between(LocalDateTime.now(), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS))
//                .toMillis() / 1000) * 20, (20 * 60) * 60); // Run every hour
    }

    public void disable() {
        getAllUniversalPlayers().forEach((UniversalPlayer up) -> {
            up.setDataLastPlayed(UniversalPlayer.getSimpleDateFormat().format(new Date()));
            up.savePlayerDataFile();
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        createUniversalPlayer(p);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = getUniversalPlayer(p);

        up.setDataLastPlayed(UniversalPlayer.getSimpleDateFormat().format(new Date()));


        up.savePlayerDataFile();
    }
}