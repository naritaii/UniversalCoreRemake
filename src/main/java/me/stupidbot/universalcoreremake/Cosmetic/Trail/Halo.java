package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import org.bukkit.Location;

public class Halo extends Trail {

    public Halo() {
        super();
    }

    @Override
    void onRun(Location loc) {
        System.currentTimeMillis();
    }

    @Override
    String getName() {
        return "Halo";
    }
}
