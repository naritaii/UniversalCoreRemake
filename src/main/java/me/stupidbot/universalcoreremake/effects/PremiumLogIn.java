package me.stupidbot.universalcoreremake.effects;

import de.myzelyam.api.vanish.VanishAPI;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static java.lang.Math.*;

public class PremiumLogIn extends Effect implements Listener {
    private final Set<Item> items = new CopyOnWriteArraySet<>();
    private boolean initialized = false;
    private int step = 0;

    public PremiumLogIn(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = 8;
    }

    @Override
    public void onRun() {
        Location loc = getLocation();
        if (!initialized) {
            loc.subtract(0, 1, 0);
            for (int i = 0; i < 8; i++) {
                Item item = loc.getWorld().dropItem(loc, new ItemStack(RandomUtils.getRandomMaterial(
                        new Material[]{Material.GOLD_INGOT, Material.GOLD_BLOCK, Material.GOLD_NUGGET,
                                Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS,
                                Material.GOLDEN_CARROT, Material.GOLDEN_APPLE, Material.GOLD_SWORD, Material.GOLD_AXE,
                                Material.GOLD_SPADE, Material.GOLD_PICKAXE, Material.GOLD_HOE, Material.GOLD_BARDING})));
                Vector v = Vector.getRandom();
                v.setX(v.getX() - 0.5f).setZ(v.getZ() - 0.5f);
                item.setVelocity(v);
                item.setPickupDelay(Integer.MAX_VALUE);
                items.add(item);
            }
            loc.add(0, 1, 0);

            new BukkitRunnable() {
                int globalStep = 0;

                @Override
                public void run() {
                    if (!items.isEmpty()) {
                        for (Item item : items)
                            if (globalStep > 12 || item.isOnGround()) {
                                items.remove(item);
                                item.remove();
                            }
                    } else
                        this.cancel();
                    globalStep++;
                }
            }.runTaskTimer(UniversalCoreRemake.getInstance(), 5, 5);

            initialized = true;
        }
        loc.subtract(0, 1, 0);


        double phi = step * (PI / 8);
        for (double theta = 0; theta <= PI * 2; theta += PI / 16) {
            double r = 1.5;
            double dx = r * cos(theta) * sin(phi);
            double dy = r * -cos(phi);
            double dz = r * sin(theta) * sin(phi);
            loc.add(dx, dy, dz);
            display(ParticleEffect.REDSTONE, loc, Color.fromRGB(255, 170, 0));
            loc.subtract(dx, dy, dz);
        }

        step++;
        if (step > 16)
            step = 0;
    }

    @EventHandler
    public void OnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("universalcore.joinparticle"))
            if (!(Bukkit.getPluginManager().isPluginEnabled("SuperVanish") ||
                    Bukkit.getPluginManager().isPluginEnabled("PremiumVanish")) || !VanishAPI.isInvisible(p)) {
                Effect joinEffect = new PremiumLogIn(UniversalCoreRemake.getEffectManager());
                joinEffect.setDynamicOrigin(new DynamicLocation(p));
                joinEffect.start();
            }
    }
}