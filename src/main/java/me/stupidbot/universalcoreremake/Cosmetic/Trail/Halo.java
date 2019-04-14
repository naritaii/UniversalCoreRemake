package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import org.bukkit.entity.Player;

public class Halo extends Trail {

    public Halo() {
        super();
    }

    void run(Player p) {
        System.currentTimeMillis();
    }

    String getName() {
        return "Halo";
    }

    String getId() { return "halo"; }

    String getDescription() { return  "A golden circle above your head."; }
}