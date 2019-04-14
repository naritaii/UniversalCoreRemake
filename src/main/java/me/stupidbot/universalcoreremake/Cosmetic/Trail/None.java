package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import org.bukkit.entity.Player;

public class None extends Trail {

    public None() { super(); }

    void run(Player p) { }

    String getName() {
        return "None";
    }

    String getId() { return "none"; }
}