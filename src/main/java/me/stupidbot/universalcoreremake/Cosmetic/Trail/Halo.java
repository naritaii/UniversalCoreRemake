package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import org.bukkit.Location;

public class Halo extends Trail {

    public Halo() {
        super();
    }

    void onRun(Location loc) {
        System.currentTimeMillis();
    }

    String getName() {
        return "Halo";
    }
}
