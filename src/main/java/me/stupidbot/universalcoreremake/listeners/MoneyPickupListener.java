package me.stupidbot.universalcoreremake.listeners;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;

public class MoneyPickupListener implements Listener {
    Random r = new Random();

    @EventHandler
    public void OnItemPickup(EntityPickupItemEvent e) {
        Entity en = e.getEntity();
        if (en instanceof Player) {
            Player p = (Player) en;
            Item entity = e.getItem();
            ItemStack item = entity.getItemStack();
            Map<String, String> meta = ItemMetadata.getMeta(item);

            if (meta.containsKey("MONEY")) {
                e.setCancelled(true);
                entity.remove();
                double money;
                String m = meta.get("MONEY");

                if (m.contains(",")) {
                    String[] range = m.split(",");
                    double rangeMin = Double.parseDouble(range[0]);
                    double rangeMax = Double.parseDouble(range[1]);
                    money = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                } else
                    money = Double.parseDouble(m);
                money = new BigDecimal(money).setScale(2, RoundingMode.HALF_UP).doubleValue();

                UniversalCoreRemake.getEconomy().depositPlayer(p, money);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&8+&6$" + TextUtils.addCommas(money)));
            }
        }

    }
}
