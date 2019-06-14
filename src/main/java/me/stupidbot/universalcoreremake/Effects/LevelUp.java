package me.stupidbot.universalcoreremake.Effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.awt.*;

public class LevelUp extends Effect {
    private int step = 0;
    private int hue = 0;
    private int initAnim = 0;
    public Player p = null;

    public LevelUp(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = 360;
    }

    public LevelUp(EffectManager effectManager, Player p) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = 360;
        this.p = p;
    }

    @Override
    public void onRun() {
        Location loc = getLocation().subtract(0, 0.5, 0);
        double dx = 0.75 * Math.sin(Math.PI / 8 * step);
        double dy = Math.sin(2 * Math.PI / 64 * step);
        double dz = 0.75 * Math.cos(Math.PI / 8 * step);
        loc.add(dx, dy, dz);

        Color rgb = Color.getHSBColor(hue / 360F, 1.0F, 1.0F);
        org.bukkit.Color color = org.bukkit.Color.fromRGB(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        hue++;
        if (hue > 360)
            hue = 0;


        display(ParticleEffect.REDSTONE, loc, color);
        if (step % 3 == 0)
            display(ParticleEffect.ENCHANTMENT_TABLE, loc, color);

        loc.subtract(dx, dy, dz);
        loc.subtract(dx, dy, dz);


        display(ParticleEffect.REDSTONE, loc, color);
        if (step % 3 == 0)
            display(ParticleEffect.ENCHANTMENT_TABLE, loc, color);


        step++;
        if (step > 64)
            step = 0;


        if (p != null && initAnim < 64) {
            if (initAnim == 0)
                TextUtils.sendTitle(p, "&6&lLEVEL UP", 5, 80, 0);
            else if (initAnim == 30)
                TextUtils.sendTitle(p, "&e&lL&6&lEVEL UP", 0, 20, 0);
            else if (initAnim == 33)
                TextUtils.sendTitle(p, "&6&lL&e&lE&6&lVEL UP", 0, 20, 0);
            else if (initAnim == 36)
                TextUtils.sendTitle(p, "&6&lLE&e&lV&6&lEL UP", 0, 20, 0);
            else if (initAnim == 39)
                TextUtils.sendTitle(p, "&6&lLEV&e&lE&6&lL UP", 0, 20, 0);
            else if (initAnim == 42)
                TextUtils.sendTitle(p, "&6&lLEVE&e&lL &6&lUP", 0, 20, 0);
            else if (initAnim == 45)
                TextUtils.sendTitle(p, "&6&lLEVEL &e&lU&6&lP", 0, 20, 0);
            else if (initAnim == 48)
                TextUtils.sendTitle(p, "&6&lLEVEL U&e&lP", 0, 20, 0);
            else if (initAnim == 51)
                TextUtils.sendTitle(p, "&6&lLEVEL UP", 0, 20, 5);
            else if (initAnim == 57)
                TextUtils.sendTitle(p, "&e&lLEVEL UP", 0, 20, 5);
            else if (initAnim == 63)
                TextUtils.sendTitle(p, "&6&lLEVEL UP", 0, 17, 5);

            initAnim++;
        }
    }
}