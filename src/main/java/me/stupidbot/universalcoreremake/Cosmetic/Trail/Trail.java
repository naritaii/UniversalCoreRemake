package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import me.stupidbot.universalcoreremake.Cosmetic.Cosmetic;
import me.stupidbot.universalcoreremake.Players.UniversalPlayer;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class Trail extends Cosmetic implements Listener {
    private List<UniversalPlayer> trailsToRun;
    private static UniversalCoreRemake plugin = UniversalCoreRemake.getInstance();

    public Trail() {
        super();
    }

    void OnRun(Location loc) { }

    String GetName() {
        return null;
    }

    public static void StartTrailsRunnable() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            UniversalPlayer p = (UniversalPlayer) all;

        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                Bukkit.broadcastMessage("t: " + System.currentTimeMillis());
            }
        }, 1, 0);
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

    }
}
