package me.stupidbot.universalcoreremake.Cosmetic.Trail;

import org.bukkit.entity.LivingEntity;

public class None extends Trail {

    public None() { super(); }

    void onRun(LivingEntity e) { }

    String getName() {
        return "None";
    }

    String getId() { return "none"; }
}