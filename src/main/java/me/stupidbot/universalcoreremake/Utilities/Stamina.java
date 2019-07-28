package me.stupidbot.universalcoreremake.Utilities;

import me.stupidbot.universalcoreremake.Managers.UniversalPlayers.UniversalPlayer;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.util.Date;

public class Stamina implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
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

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

        if (up.firstJoin())
            up.setStamina(Stamina.getMaxStamina(1));
        else
            try {
                if (new Date().getTime() - UniversalPlayer.getSimpleDateFormat().parse(up.getLastPlayed())
                        .getTime() >= 6.48e+7) { // 18 hours
                    up.setStamina(Stamina.getMaxStamina(up.getLevel()));
                    p.setFoodLevel(20);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&a&lALL STAMINA RECOVERED!&7&o All stamina is recovered when you've been " +
                                    "offline for 18+ hours."));
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
    }

    private static void addStamina(Player p, int i) {
        setStamina(p, Math.min(getStamina(p) + i, getMaxStamina(p)));
    }

    public static void removeStamina(Player p, int i) {
        setStamina(p, Math.max(getStamina(p) - i, 0));
    }

    public static int getStamina(Player p) {
        return UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getStamina();
    }

    private static void setStamina(Player p, int i) {
        UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).setStamina(i);
        updateStamina(p);
    }

    private static void updateStamina(Player p) {
        p.setFoodLevel((int) Math.floor((getStamina(p) * 20d) / getMaxStamina(p)));
    }

    public static int getMaxStamina(Player p) {
        return getMaxStamina(UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getLevel());
    }

    public static int getMaxStamina(int i) {
        return (i * 5) + 50;
    }



    enum BaseFoodStamina {
        ROTTEN_FLESH(5);

        final int baseFoodStamina;

        BaseFoodStamina(int baseFoodStamina) {
            this.baseFoodStamina = baseFoodStamina;
        }

        int getBaseFoodStamina() {
            return baseFoodStamina;
        }
    }
}