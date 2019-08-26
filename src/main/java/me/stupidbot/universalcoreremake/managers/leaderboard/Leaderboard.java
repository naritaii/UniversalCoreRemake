package me.stupidbot.universalcoreremake.managers.leaderboard;

import org.bukkit.Location;

import java.util.Map;

class Leaderboard {

   private Map<String, Comparable> sortedMap;
   private final Location loc;
   private final String id;

    Leaderboard(Map<String, Comparable> sortedMap, Location loc, String id) {
        this.sortedMap = sortedMap;
        this.loc = loc;
        this.id = id;
    }

    public Map<String, Comparable> getSortedMap() {
        return sortedMap;
    }

    public void setSortedMap(Map<String, Comparable> sortedMap) {
        this.sortedMap = sortedMap;
    }

    public Location getLoc() {
        return loc;
    }

    public String getId() {
        return id;
    }
}
