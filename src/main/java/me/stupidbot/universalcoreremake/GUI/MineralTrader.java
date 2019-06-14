package me.stupidbot.universalcoreremake.GUI;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.SellItem;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class MineralTrader extends UniversalGUI {

    public MineralTrader() {
        super(54, "Mineral Trader");
        Inventory inv = this.getInventory();
        setGlass();

        List<SellItem> items = Arrays.asList(
                new SellItem(Material.RED_SANDSTONE, 1),
                new SellItem(Material.SANDSTONE, 3));
        ListIterator<SellItem> iterator = items.listIterator();
        for (int i = 10; i < 45; i++)
            if (!iterator.hasNext())
                break;
            else {
                if (inv.getItem(i) == null) {
                    SellItem si = iterator.next();
                    ItemStack icon = si.getItem();
                    Material m = icon.getType();
                    ItemMeta iconMeta = icon.getItemMeta();
                    double cost = si.getSellCost();

                    String mName = TextUtils.capitalizeFully(m.toString());
                    iconMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            "&a" + mName));
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("");
                    lore.add(ChatColor.translateAlternateColorCodes('&',
                            "&7Price:"));
                    lore.add(ChatColor.translateAlternateColorCodes('&',
                            "&6$" + TextUtils.addCommas(cost)));
                    lore.add("");
                    lore.add(ChatColor.translateAlternateColorCodes('&',
                            "&eClick to sell!"));
                    iconMeta.setLore(lore);
                    icon.setItemMeta(iconMeta);

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
                                    "&aYou should x" + TextUtils.addCommas(amt) + " " + mName +
                                    " for &6$" + TextUtils.addCommas(money)));
                        } else
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cYou don't have any ") + mName);
                    });
                }
            }
    }

    private void setGlass() {
        setItem(0, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(1, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(2, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(3, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(4, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(5, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(6, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(7, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(8, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));

        setItem(9, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(18, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(27, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(36, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));

        setItem(17, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(26, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(35, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(44, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));

        setItem(45, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(46, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(47, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(48, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(49, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(50, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(51, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(52, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        setItem(53, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
    }
}
