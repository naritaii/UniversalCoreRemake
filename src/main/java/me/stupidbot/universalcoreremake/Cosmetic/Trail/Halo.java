package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import org.bukkit.entity.LivingEntity;

public class Halo extends Trail {

    public Halo() {
        super();
    }

    void onRun(LivingEntity e) {
        System.currentTimeMillis();
    }

    String getName() {
        return "Halo";
    }

    String getId() { return "halo"; }
}