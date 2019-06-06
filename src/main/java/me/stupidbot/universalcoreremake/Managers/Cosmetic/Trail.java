package me.stupidbot.universalcoreremake.Managers.Cosmetic;

import de.slikey.effectlib.Effect;
import org.bukkit.entity.Player;

public enum Trail {
    NONE(null),
    HALO("me.stupidbot.universalcoreremake.Effects.Trails.Halo");

    private String reference;
    Trail(String reference) {
        this.reference = reference;
    }

    private void set(Player p) {
        try {
            Effect eff = (Effect) Class.forName(reference).newInstance();
            // set effect
        } catch (Exception e) {
            // set no effect
        }
    }
}