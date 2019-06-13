package me.stupidbot.universalcoreremake.Effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Sound;

import static de.slikey.effectlib.util.MathUtils.random;

public class EnhancedBlockBreak extends Effect {

    public EnhancedBlockBreak(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        particleOffsetX = 0.5f;
        particleOffsetY = 0.5f;
        particleOffsetZ = 0.5f;
    }

    @Override
    public void onRun() {
        Location loc = getLocation().add(getLocation().getX() < 0 ? 0.5 : -0.5, 0.5,
                getLocation().getZ() < 0 ? 0.5 : -0.5);
        for (int i = 0; i < 10; i++) {
            double radius = 1;
            double u = random.nextDouble();
            double v = random.nextDouble();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);
            double dx = radius * Math.sin(phi) * Math.cos(theta);
            double dy = radius * Math.sin(phi) * Math.sin(theta);
            double dz = radius * Math.cos(phi);

            display(ParticleEffect.REDSTONE, loc.clone().add(dx, dy, dz), 1f, 25);
        }
        loc.getWorld().playSound(loc, Sound.FIREWORK_LAUNCH, 1f, 2f);
    }
}