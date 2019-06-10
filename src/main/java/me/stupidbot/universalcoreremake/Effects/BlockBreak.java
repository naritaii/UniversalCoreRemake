package me.stupidbot.universalcoreremake.Effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Sound;

public class BlockBreak extends Effect {

    public BlockBreak(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        particleOffsetX = 0.4f;
        particleOffsetY = 0.4f;
        particleOffsetZ = 0.4f;
    }

    @Override
    public void onRun() {
        Location loc = getLocation().add(getLocation().getX() < 0 ? 0.5 : -0.5, 0.5,
                getLocation().getZ() < 0 ? 0.5 : -0.5);
        display(ParticleEffect.BLOCK_CRACK, loc, 1f, 25);
        loc.getWorld().playSound(loc, Sound.DIG_STONE, 1f, 1f);
    }
}