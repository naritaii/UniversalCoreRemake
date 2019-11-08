package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
import me.stupidbot.universalcoreremake.utilities.item.SellItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Sell implements InventoryProvider {
    private final List<SellItem> items;
    private final String title;

    private Sell(String title, List<SellItem> items) {
        this.items = items;
        this.title = title;
    }

    public static SmartInventory getInventory(String title, List<SellItem> items) {
        return SmartInventory.builder()
                .id(title.toLowerCase().replaceAll("\\s",""))
                .provider(new Sell(title, items))
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(6, 9)
                .title(title).build();
    }

    private final List<ClickableItem> clickableItems = new ArrayList<>();
    // private int amt = 0;

    @Override
    public void init(Player p, InventoryContents contents) {
        if (clickableItems.isEmpty())
            for (SellItem si : items) {
                double cost = si.getSellCost();
                ItemStack[] tradeItems = si.getTradeItems();
                Material m = si.getType();
                String mName = TextUtils.capitalizeFully(m.toString());
                ItemBuilder icon = new ItemBuilder(si.getDisplayItem())
                        .name("&a" + (si.getDisplayItem().getItemMeta().hasDisplayName() ?
                                si.getDisplayItem().getItemMeta().getDisplayName() : mName))
                        .lore("")
                        .lore("&7Sell Price:");

                    if (si.hasSellCost())
                        icon.lore("&6$" + TextUtils.addCommas(cost));
                    else
                        for (ItemStack tradeItem : tradeItems) {
                            String item = TextUtils.capitalizeFully(tradeItem.getType().toString());
                            if (tradeItem.hasItemMeta() && tradeItem.getItemMeta().hasDisplayName())
                                item = tradeItem.getItemMeta().getDisplayName();
                            icon.lore("&7" + tradeItem.getAmount() + "x &e" + item);
                        }


                        icon.lore("")
                        .lore("&eClick to sell!");


                clickableItems.add(ClickableItem.of(icon.build(), e -> {
                    Inventory pinv = p.getInventory();


                    if (pinv.contains(m)) {
                        int amt = 0;
                        for (ItemStack item : pinv.getContents())
                            if (item != null && item.getType() == m)
                                amt += item.getAmount();

                        pinv.remove(m);

                        if (si.hasSellCost()) {
                            double money = amt * cost;
                            UniversalCoreRemake.getEconomy().depositPlayer(p, money);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&aYou sold &3x" + TextUtils.addCommas(amt) + "&6 " + mName +
                                            "&a for &6$" + TextUtils.addCommas(money)));
                        } else {
                            for(int i = 0; i < amt; i++)
                                for (ItemStack tradeItem : tradeItems)
                                    ItemUtils.addItemSafe(p, tradeItem);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&aYou sold &3x" + TextUtils.addCommas(amt) + "&6 " + mName));
                        }



                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cYou don't have any ") + mName + "!");
                        p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                    }
                }));
            }


        Pagination pagination = contents.pagination();
        int perPage = 28;
        pagination.setItemsPerPage(perPage);
        pagination.setItems(clickableItems.toArray(new ClickableItem[0]));
        ItemStack border = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
                .name(" ").build();
        contents.fillBorders(ClickableItem.empty(border));


        for (ClickableItem ci : pagination.getPageItems())
            contents.add(ci);


        int pages = Math.floorDiv(clickableItems.size() - 1, perPage);


        if (!pagination.isFirst())
            contents.set(5, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).name("&ePrevious Page").build(),
                    e -> getInventory(title, items).open(p, pagination.previous().getPage())));
        if (pagination.getPage() < pages)
            contents.set(5, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).name("&eNext Page").build(),
                    e -> getInventory(title, items).open(p, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}