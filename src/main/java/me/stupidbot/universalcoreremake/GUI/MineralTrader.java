package me.stupidbot.universalcoreremake.GUI;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.ItemBuilder;
import me.stupidbot.universalcoreremake.Utilities.SellItem;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class MineralTrader extends UniversalGUI {

    public MineralTrader() {
        super(54, "Mineral Trader");
        Inventory inv = getInventory();
        setGlass();

        List<SellItem> items = Arrays.asList(
                new SellItem(Material.RED_SANDSTONE, 0.70),
                new SellItem(Material.SANDSTONE, 2.00));
        ListIterator<SellItem> iterator = items.listIterator();
        for (int i = 10; i < 45; i++)
            if (!iterator.hasNext())
                break;
            else if (inv.getItem(i) == null) {
                    SellItem si = iterator.next();
                    double cost = si.getSellCost();
                    Material m = si.getType();
                    String mName = TextUtils.capitalizeFully(m.toString());
                    ItemStack icon = new ItemBuilder(si.getItem())
                            .name("&a" + mName)
                            .lore("")
                            .lore("&7Sell Price:")
                            .lore("&6$" + TextUtils.addCommas(cost))
                            .lore("")
                            .lore("&eClick to sell!").build();


                    setItem(i, icon, player -> {
                        Inventory pinv = player.getInventory();

                        if (pinv.contains(m)) {
                            int amt = 0;
                            for (ItemStack item : pinv.getContents())
                                if ((item != null) && (item.getType() == m))
                                    amt += item.getAmount();

                            pinv.remove(m);
                            double money = amt * cost;
                            UniversalCoreRemake.getEconomy().depositPlayer(player, money);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&aYou sold &3x" + TextUtils.addCommas(amt) + " " + mName +
                                    "&a for &6$" + TextUtils.addCommas(money)));
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                        } else
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cYou don't have any ") + mName);
                    });
                }
    }

    private void setGlass() {
        ItemStack i = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
                .name(" ").build();

        setItem(0, i);
        setItem(1, i);
        setItem(2, i);
        setItem(3, i);
        setItem(4, i);
        setItem(5, i);
        setItem(6, i);
        setItem(7, i);
        setItem(8, i);

        setItem(9, i);
        setItem(18, i);
        setItem(27, i);
        setItem(36, i);

        setItem(17, i);
        setItem(26, i);
        setItem(35, i);
        setItem(44, i);

        setItem(45, i);
        setItem(46, i);
        setItem(47, i);
        setItem(48, i);
        setItem(49, i);
        setItem(50, i);
        setItem(51, i);
        setItem(52, i);
        setItem(53, i);
    }
}
