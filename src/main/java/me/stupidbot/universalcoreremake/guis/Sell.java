package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.SellItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sell implements InventoryProvider {
    private final List<SellItem> items;

    private Sell(List<SellItem> items) {
        this.items = items;
    }

    public static SmartInventory getInventory(String title, List<SellItem> items) {
        return SmartInventory.builder()
                .id(UUID.randomUUID().toString())
                .provider(new Sell(items))
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(6, 9)
                .title(title).build();
    }

    private final int page = 1;
    private final List<ClickableItem> clickableItems = new ArrayList<>();
    private int amt = 0;

    @Override
    public void init(Player p, InventoryContents contents) {
        if (clickableItems.isEmpty())
            for (SellItem si : items) {
                double cost = si.getSellCost();
                Material m = si.getType();
                String mName = TextUtils.capitalizeFully(m.toString());
                ItemStack icon = new ItemBuilder(si.getDisplayItem())
                        .name("&a" + (si.getDisplayItem().getItemMeta().hasDisplayName() ?
                                si.getDisplayItem().getItemMeta().getDisplayName() : mName))
                        .lore("")
                        .lore("&7Sell Price:")
                        .lore("&6$" + TextUtils.addCommas(cost))
                        .lore("")
                        .lore("&eClick to sell!").build();


                clickableItems.add(ClickableItem.of(icon, e -> {
                    Inventory pinv = p.getInventory();


                    if (pinv.contains(m)) {
                        int amt = 0;
                        for (ItemStack item : pinv.getContents())
                            if ((item != null) && (item.getType() == m))
                                amt += item.getAmount();

                        pinv.remove(m);
                        double money = amt * cost;
                        UniversalCoreRemake.getEconomy().depositPlayer(p, money);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aYou sold &3x" + TextUtils.addCommas(amt) + "&6 " + mName +
                                        "&a for &6$" + TextUtils.addCommas(money)));
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cYou don't have any ") + mName + "!");
                        p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                    }
                }));
            }


        ItemStack border = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
                .name(" ").build();
        contents.fillBorders(ClickableItem.empty(border)); // 28 items per page, 4 rows of 7
        updateItems(contents);
    }

    private void updateItems(InventoryContents contents) {
        contents.fillRect(1, 1, 4, 7, null);
        for (int i = 0; i < Math.min(28, clickableItems.size()); i++)
            contents.add(clickableItems.get(i));
    }



    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}