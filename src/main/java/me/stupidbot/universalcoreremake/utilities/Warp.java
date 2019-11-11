package me.stupidbot.universalcoreremake.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Warp {
    private final Location loc;
    private final  String id;
    private final String name;
    private static final List<Warp> warps = new ArrayList<>();
    private static final Map<String, Integer> warpsDictionary = new HashMap<>();

    static {
        Warp aoba = new Warp(new Location(Bukkit.getWorld("world"), -92.5d, 48d, -69d, -120f, -10f),
                "aoba", "Aoba Mine");
        warpsDictionary.put(aoba.getId(), warps.size());
        warps.add(aoba);
        Warp mountainsVillage = new Warp(new Location(Bukkit.getWorld("world"), -105d, 4d, 91d, 135f, 0f),
                "mountains_village", "Village");
        warpsDictionary.put(mountainsVillage.getId(), warps.size());
        warps.add(mountainsVillage);
    }

    public static List<Warp> getWarps() {
        return warps;
    }

    public static Map<String, Integer>  getWarpsDictionary() {
        return warpsDictionary;
    }

    public static Warp getWarpFromId(String id) {
        if (id != null && warpsDictionary.containsKey(id))
            return warps.get(warpsDictionary.get(id));

        return warps.get(0); // Defaults to Aoba Mine if there's an error
    }

    public Warp(Location loc, String id, String name) {
        this.loc = loc;
        this.id = id;
        this.name = name;
    }

    public Location getLocation() {
        return loc;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void warp(Player p) {
        p.teleport(loc);
        p.playSound(loc, Sound.PORTAL_TRAVEL, 1f, 1f);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&eTeleporting..."));
    }
}
