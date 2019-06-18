package me.stupidbot.universalcoreremake.Utilities;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class Stamina implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        ItemStack i = e.getItem();
        addStamina(p, BaseFoodStamina.valueOf(i.getType().toString()).getBaseFoodStamina());
    }

    public static void addStamina(Player p, int i) {
        setStamina(p, Math.min(getStamina(p) + i, getMaxStamina(p)));
    }

    public static void removeStamina(Player p, int i) {
        setStamina(p, Math.max(getStamina(p) - i, 0));
    }

    public static int getStamina(Player p) {
        return UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getDataStamina();
    }

    public static void setStamina(Player p, int i) {
        UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).setDataStamina(i);
        updateStamina(p);
    }

    private static void updateStamina(Player p) {
        p.setFoodLevel((int) Math.floor((getStamina(p) * 20d) / getMaxStamina(p)));
    }

    public static int getMaxStamina(Player p) {
        return getMaxStamina(UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getDataLevel());
    }

    public static int getMaxStamina(int i) {
        return (i * 5) + 50;
    }



    enum BaseFoodStamina {
        ROTTEN_FLESH(5);

        int baseFoodStamina;

        BaseFoodStamina(int baseFoodStamina) {
            this.baseFoodStamina = baseFoodStamina;
        }

        int getBaseFoodStamina() {
            return baseFoodStamina;
        }
    }
}