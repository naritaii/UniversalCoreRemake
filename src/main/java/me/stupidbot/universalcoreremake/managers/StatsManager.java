package me.stupidbot.universalcoreremake.managers;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.armorequip.ArmorEquipEvent;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class StatsManager implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void OnFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerItemConsume(PlayerItemConsumeEvent e) {
        ItemStack i = e.getItem();
        if (i.getType() != Material.POTION) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            p.setItemInHand(ItemUtils.removeItem(p.getItemInHand(), 1));
            int stamina = BaseFoodStamina.valueOf(i.getType().toString()).getBaseFoodStamina();
            addStamina(p, stamina);
            TextUtils.sendActionbar(p, "&3Stamina &a+" + stamina +
                    " &e(" + getStamina(p) + "/" + getMaxStamina(p) + ")");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

        double maxHealth = getMaxHealth(p);
        p.setMaxHealth(maxHealth);


        if (up.firstJoin()) {
            up.setStamina(getMaxStamina(p));
            p.setHealth(maxHealth);
        } else
            try {
                if (new Date().getTime() - UniversalPlayer.getSimpleDateFormat().parse(up.getLastPlayed())
                        .getTime() >= 6.48e+7) { // 18 hours
                    up.setStamina(getMaxStamina(p));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&a&lALL STAMINA RECOVERED!&7&o All stamina is recovered when you've been " +
                                    "offline for 18+ hours."));
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }

        UniversalCoreRemake plugin = UniversalCoreRemake.getInstance();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            updateHealth(p);
            updateStamina(p);
            p.setWalkSpeed(getSpeed(p));
        });

    }

    @EventHandler
    public void OnArmorEquip(ArmorEquipEvent e) {
            updateHealth(e.getPlayer());
            updateStamina(e.getPlayer());
            e.getPlayer().setWalkSpeed(getSpeed(e.getPlayer()));
    }

    private void addStamina(Player p, int i) {
        setStamina(p, Math.min(getStamina(p) + i, getMaxStamina(p)));
    }

    public void removeStamina(Player p, int i) {
        setStamina(p, Math.max(getStamina(p) - i, 0));
    }

    public int getStamina(Player p) {
        return UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getStamina();
    }

    private void setStamina(Player p, int i) {
        UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).setStamina(i);
        updateStamina(p);
    }

    private void updateStamina(Player p) {
        p.setFoodLevel((int) Math.floor((getStamina(p) * 20d) / getMaxStamina(p)));
    }

    public int getMaxStamina(Player p) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        int boost = 25;
        for (ItemStack i : p.getInventory().getArmorContents()) {
                Map<String, String> meta = ItemMetadata.getMeta(i);
                if (meta.containsKey("STAMINA"))
                    boost += Integer.parseInt(meta.get("STAMINA"));
            }

        int staminaPerLevel = 5;
        return ((up.getLevel() - 1) * staminaPerLevel) + boost;
    }

    public float getDisplayedSpeed(Player p) {
        float base = getSpeed(p);

        for (PotionEffect effect : p.getActivePotionEffects())
            if (effect.getType() == PotionEffectType.SPEED) {
                base += (effect.getAmplifier() + 1) * 0.2f;
                break;
            }

        return (base + 0.8f) * 100;
    }

    private float getSpeed(Player p) {
        float spd = 0.2f;

        for (ItemStack i : p.getInventory().getArmorContents()) {
                Map<String, String> meta = ItemMetadata.getMeta(i);
                if (meta.containsKey("SPEED"))
                    spd += Float.parseFloat(meta.get("SPEED"));
            }

        return spd;
    }

    private double getMaxHealth(Player p) {
        double boost = 0d;
        for (ItemStack i : p.getInventory().getArmorContents()) {
                Map<String, String> meta = ItemMetadata.getMeta(i);
                if (meta.containsKey("HEALTH"))
                    boost += Double.parseDouble(meta.get("HEALTH"));
            }

        return 100d + boost;
    }

    private void updateHealth(Player p) {
        double maxHealth = getMaxHealth(p);
        p.setHealthScale(Math.floor(maxHealth / 5));
    }

    @EventHandler
    public void OnDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            e.setDamage(calculateDamage(p, e.getDamage()));
        }
    }

    private double calculateDamage(Player p, double damage) {
        int defensePoints = getDefense(p);


        return damage * (100 - (defensePoints / 100d));
    }

    public int getDefense(Player p) {
        int defensePoints = 0;
        for (ItemStack i : p.getInventory().getArmorContents()) {
                Map<String, String> meta = ItemMetadata.getMeta(i);
                if (meta.containsKey("DEFENSE"))
                    defensePoints += Integer.parseInt(meta.get("DEFENSE"));
            }

        return defensePoints;
    }

    enum BaseFoodStamina {
        ROTTEN_FLESH(5), APPLE(20), BREAD(50);

        final int baseFoodStamina;

        BaseFoodStamina(int baseFoodStamina) {
            this.baseFoodStamina = baseFoodStamina;
        }

        int getBaseFoodStamina() {
            return baseFoodStamina;
        }
    }
}