package me.stupidbot.universalcoreremake.effects.trail;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Color;
import org.bukkit.Location;

class Halo extends Effect {
    private int step = 0;

    public Halo(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = -1; // Infinite by default;
    }

    @Override
    public void onRun() {
        Location loc = getLocation();

        double dx = 0.75 * Math.sin(Math.PI / 8 * step);
        double dy = 2;
        double dz = 0.75 * Math.cos(Math.PI / 8 * step);
        loc.add(dx, dy, dz);

        display(ParticleEffect.REDSTONE, loc, Color.fromRGB(255, 170, 0));


        step++;
        if (step > 8)
            step = 0;
    }
}