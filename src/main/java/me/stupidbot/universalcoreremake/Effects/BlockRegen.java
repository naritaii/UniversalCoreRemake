package me.stupidbot.universalcoreremake.Effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import me.stupidbot.universalcoreremake.Utilities.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Sound;

public class BlockRegen extends Effect {

    public BlockRegen(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        particleOffsetX = 0.7f;
        particleOffsetY = 0.7f;
        particleOffsetZ = 0.7f;
    }

    @Override
    public void onRun() {
        Location loc = LocationUtils.center(getLocation());
        display(ParticleEffect.FLAME, loc, 0f, 20);
        loc.getWorld().playSound(loc, Sound.DRINK, 0.5f, 1.5f);
    }
}