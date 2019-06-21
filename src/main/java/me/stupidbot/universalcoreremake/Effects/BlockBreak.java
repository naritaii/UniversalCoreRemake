package me.stupidbot.universalcoreremake.Effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import me.stupidbot.universalcoreremake.Utilities.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Sound;

public class BlockBreak extends Effect {

    public BlockBreak(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        particleOffsetX = 0.5f;
        particleOffsetY = 0.5f;
        particleOffsetZ = 0.5f;
    }

    @Override
    public void onRun() {
        Location loc = LocationUtils.center(getLocation());
        display(ParticleEffect.BLOCK_CRACK, loc, 1f, 25);
        loc.getWorld().playSound(loc, Sound.DIG_STONE, 1f, 1f);
    }
}