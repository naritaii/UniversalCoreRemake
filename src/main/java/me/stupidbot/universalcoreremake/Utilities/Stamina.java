package me.stupidbot.universalcoreremake.Utilities;

import me.stupidbot.universalcoreremake.Managers.UniversalPlayers.UniversalPlayer;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.ParseException;
import java.util.Date;

public class Stamina implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        try {
            if (up.firstJoin() || new Date().getTime() - up.getSimpleDateFormat().parse(up.getDataLastPlayed())
                    .getTime() >= 6.48e+7) // 18 hours
                setStamina(p, getMaxStamina(p));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e)
    {
        e.setCancelled(true);
    }

    public static void addStamina(Player p, int i) {
        setStamina(p, Math.min(getMaxStamina(p), getStamina(p) + i));
    }

    public static void removeStamina(Player p, int i) {
        setStamina(p, Math.max(0, getStamina(p) - i));
    }

    public static int getStamina(Player p) {
        return UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getDataStamina();
    }

    private static void setStamina(Player p, int i) {
        UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).setDataStamina(i);
        updateStamina(p);
    }

    private static void updateStamina(Player p) {
        p.setFoodLevel((int) Math.floor((getStamina(p) * 20d) / getMaxStamina(p)));
    }

    public static int getMaxStamina(Player p) {
        return (UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p)
                .getDataLevel() * 10) + 100;
    }
}