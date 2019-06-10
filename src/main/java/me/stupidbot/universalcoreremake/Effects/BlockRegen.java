package me.stupidbot.universalcoreremake.Effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Sound;

public class BlockRegen extends Effect {

    public BlockRegen(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        particleOffsetX = 0.8f;
        particleOffsetY = 0.8f;
        particleOffsetZ = 0.8f;
    }

    @Override
    public void onRun() {
        Location loc = getLocation().add(getLocation().getX() < 0 ? 0.5 : -0.5, 0.5,
                getLocation().getZ() < 0 ? 0.5 : -0.5);
        display(ParticleEffect.FLAME, loc, 0f, 25);
        loc.getWorld().playSound(loc, Sound.DRINK, 0.5f, 2f);
    }
}