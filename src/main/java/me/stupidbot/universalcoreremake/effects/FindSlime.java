package me.stupidbot.universalcoreremake.effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import me.stupidbot.universalcoreremake.utilities.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Sound;

public class FindSlime extends Effect {

    public FindSlime(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        particleOffsetX = 0.7f;
        particleOffsetY = 0.7f;
        particleOffsetZ = 0.7f;
    }

    @Override
    public void onRun() {
        Location loc = LocationUtils.center(getLocation());
        display(ParticleEffect.BLOCK_CRACK, loc, 0f, 20);
        getTargetPlayer().playSound(loc, Sound.SLIME_WALK, 1f, 1f);
    }
}