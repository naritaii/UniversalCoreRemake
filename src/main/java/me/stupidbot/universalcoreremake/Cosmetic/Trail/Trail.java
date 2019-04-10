package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import me.stupidbot.universalcoreremake.Cosmetic.Cosmetic;
import me.stupidbot.universalcoreremake.Players.UniversalPlayer;
import me.stupidbot.universalcoreremake.Players.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Trail extends Cosmetic implements Listener {
    private static HashMap<UUID, String> trailsToRun = new HashMap<UUID, String>();
    private static List<Trail> registeredTrails = new ArrayList<Trail>();
    private static HashMap<String, Integer> registeredTrailsDictionary = new HashMap<String, Integer>();

    public Trail() {
        super();
    }

    private static List<Trail> getTrails() {
        return registeredTrails;
    }

    private static HashMap<String, Integer> getTrailsDictionary() {
        return registeredTrailsDictionary;
    }

    private Trail getTrail(String id) {
        List<Trail> trails = getTrails();
        HashMap<String, Integer> trailsDictionary = getTrailsDictionary();

        return trailsDictionary.get(trails.get(id));
    }

    private static void registerTrail(Trail... trails) {
        Trail[] arrayOfTrails;

        int j = (arrayOfTrails = trails).length;
        for (int i = 0; i < j; i++) {
            Trail trail = arrayOfTrails[i];

            List<Trail> trails = getTrails();
            registeredTrailsDictionary.put(trail.getId(), trails.size());
            trails.add(trail);
        }
    }

    private static String addOrUpdateTrailToRun(Player p) {
        String trail = UniversalPlayerManager.getUniversalPlayer(p).getPlayerDataCosmeticIdTrail();

        trailsToRun.put(p.getUniqueId(), trail);

        return trail;
    }

    private String getTrailToRun(Player p) {
        String trail = trailsToRun.get(p.getUniqueId());

        if (trail == null)
            trail = addOrUpdateTrailToRun(p);

        return trail;
    }

    public static void startTrailsRunnable() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            UniversalPlayer up = (UniversalPlayer) all;

            if (!up.getPlayerDataCosmeticIdTrail().equals("none"))
                addOrUpdateTrailToRun(all);
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(UniversalCoreRemake.getInstance(), new Runnable() {
            public void run() {
                for ()
            }
        }, 1, 0);
    }

    public static void onEnable() {
        registerTrail(new None(), new Halo());
        startTrailsRunnable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalPlayerManager.getUniversalPlayer(p);

        if (!up.getPlayerDataCosmeticIdTrail().equals("none"))
            addOrUpdateTrailToRun(p);
    }

    void onRun(LivingEntity e) { }

    String getName() {
        return null;
    }

    String getId() {
        return null;
    }
}