package me.stupidbot.universalcoreremake.listeners;

import me.stupidbot.universalcoreremake.utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;

public class XpPickupListener implements Listener {
    private final Random r = new Random();

    @EventHandler
    public void OnItemPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        Item entity = e.getItem();
        ItemStack item = entity.getItemStack();
        Map<String, String> meta = ItemMetadata.getMeta(item);

        if (meta.containsKey("XP")) {
            e.setCancelled(true);
            entity.remove();
            double xp;
            String m = meta.get("XP");

            if (m.contains("_")) {
                String[] range = m.split("_");
                double rangeMin = Double.parseDouble(range[0]);
                double rangeMax = Double.parseDouble(range[1]);
                xp = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            } else
                xp = Double.parseDouble(m);
            xp = new BigDecimal(xp).setScale(0, RoundingMode.HALF_UP).doubleValue(); // TODO just get a random int

            PlayerLevelling.giveXp(p, (int) xp);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8+&b" + TextUtils.addCommas(xp) + " XP"));
        }
    }

    @EventHandler
    public void OnItemMerge(ItemMergeEvent e) {
        Item entity = e.getEntity();
        ItemStack item = entity.getItemStack();
        Map<String, String> meta = ItemMetadata.getMeta(item);

        if (meta.containsKey("XP"))
            e.setCancelled(true);
    }

    @EventHandler
    public void OnPickupExp(PlayerExpChangeEvent e) {
        Player p = e.getPlayer();
        int xp = e.getAmount();
        e.setAmount(0); // Essentially cancel the event and handle it myself
        PlayerLevelling.giveXp(p, xp);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8+&b" + TextUtils.addCommas(xp) + " XP"));
    }
}
