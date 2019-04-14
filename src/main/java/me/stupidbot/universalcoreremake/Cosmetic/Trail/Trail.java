package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import me.stupidbot.universalcoreremake.Cosmetic.Cosmetic;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.Players.UniversalPlayer;
import me.stupidbot.universalcoreremake.Utilities.Players.UniversalPlayerManager;
import org.bukkit.Bukkit;
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

    private static HashMap<UUID, String> getTrailsToRun() {
        return trailsToRun;
    }

    private static List<Trail> getTrails() {
        return registeredTrails;
    }

    private static HashMap<String, Integer> getTrailsDictionary() {
        return registeredTrailsDictionary;
    }

    private static Trail getTrail(String id) {
        List<Trail> trails = getTrails();
        HashMap<String, Integer> trailsDictionary = getTrailsDictionary();

        return trails.get(trailsDictionary.get(id));
    }

    private static void registerTrail(Trail... trails) {
        Trail[] arrayOfTrails;

        int j = (arrayOfTrails = trails).length;
        for (int i = 0; i < j; i++) {
            Trail trail = arrayOfTrails[i];

            List<Trail> allTrails = getTrails();
            registeredTrailsDictionary.put(trail.getId(), allTrails.size());
            allTrails.add(trail);
        }
    }

    private static String addOrUpdateTrailToRun(Player p) {
        String trail = UniversalPlayerManager.getUniversalPlayer(p).getPlayerDataCosmeticIdTrail();

        trailsToRun.put(p.getUniqueId(), trail);

        return trail;
    }

    public static String addOrUpdateTrailToRun(Player p, String id) {
       if (getTrail(id) != null)
           trailsToRun.put(p.getUniqueId(), id);
       else
           throw new IllegalArgumentException("id (\"" + id + "\") is not a registered trail");

        return id;
    }

    private static void removeTrailToRun(Player p) {
        trailsToRun.remove(p.getUniqueId());
    }

    private static String getTrailToRun(Player p) {
        String trail = trailsToRun.get(p.getUniqueId());

        if (trail == null)
            trail = addOrUpdateTrailToRun(p);

        return trail;
    }

    private static void startTrailsRunnable() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            UniversalPlayer up = (UniversalPlayer) all;

            if (!up.getPlayerDataCosmeticIdTrail().equals("none"))
                addOrUpdateTrailToRun(all);
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(UniversalCoreRemake.getInstance(), new Runnable() {
            public void run() {
               for (UUID id : getTrailsToRun().keySet()) {
                   Player p = Bukkit.getPlayer(id);

                   if (p.isOnline())
                        getTrail(getTrailToRun(p)).run(p);
                   else
                       removeTrailToRun(p);
               }
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

    void run(Player p) { }

    String getName() {
        return null;
    }

    String getId() {
        return null;
    }
}