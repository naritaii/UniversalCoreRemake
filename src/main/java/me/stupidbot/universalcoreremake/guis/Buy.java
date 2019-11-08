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
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Buy implements InventoryProvider {
    private final List<SellItem> items;
    private final String title;

    private Buy(List<SellItem> items, String title) {
        this.items = items;
        this.title = title;
    }

    public static SmartInventory getInventory(String title, List<SellItem> items) {
        return SmartInventory.builder()
                .id(title.toLowerCase().replaceAll("\\s", ""))
                .provider(new Buy(items, title))
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(6, 9)
                .title(title).build();
    }

    private final List<ClickableItem> clickableItems = new ArrayList<>();
    // private final int amt = 1;

    @Override
    public void init(Player p, InventoryContents contents) {
        int amt = 1;

        if (clickableItems.isEmpty())
            for (SellItem si : items) {
                double cost = si.getSellCost() * amt;
                ItemStack[] tradeItems = si.getTradeItems();

                Material m = si.getType();
                String mName = TextUtils.capitalizeFully(m.toString());
                String dName = (si.getDisplayItem().getItemMeta().hasDisplayName() ?
                        si.getDisplayItem().getItemMeta().getDisplayName() : mName);
                ItemBuilder icon = new ItemBuilder(si.getDisplayItem())
                        .amount(amt)
                        .name("&a" + dName)
                        .lore("")
                        .lore("&7Cost:");

                if (si.hasSellCost())
                    icon.lore("&6$" + TextUtils.addCommas(cost));
                else
                    for (ItemStack tradeItem : tradeItems) {
                        String item = TextUtils.capitalizeFully(tradeItem.getType().toString());
                        if (tradeItem.hasItemMeta() && tradeItem.getItemMeta().hasDisplayName())
                            item = tradeItem.getItemMeta().getDisplayName();
                        icon.lore("&7" + TextUtils.addCommas(tradeItem.getAmount() * amt) + "x &e" + item);
                    }

                icon.lore("")
                        .lore("&eClick to buy!");

                clickableItems.add(ClickableItem.of(icon.build(), e -> {
                    if (si.hasSellCost()) {
                        Economy econ = UniversalCoreRemake.getEconomy();
                        if (econ.getBalance(p) >= cost) {
                            UniversalCoreRemake.getEconomy().withdrawPlayer(p, cost);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&aYou bought &3" + amt + "x&6 " + dName +
                                            "&a for &c$" + TextUtils.addCommas(cost)));
                            ItemUtils.addItemSafe(p, si.getItem());
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cYou don't have enough money!"));
                            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                        }
                    } else {
                        Inventory pinv = p.getInventory();


                        boolean hasItems = true;
                        for (ItemStack tradeItem : tradeItems) {
                            int has = 0;
                            for (ItemStack item : pinv.getContents()) {
                                if (item != null && item.isSimilar(tradeItem))
                                    has += item.getAmount();
                            }
                            hasItems = has >= tradeItem.getAmount() * amt;

                            if (!hasItems)
                                break;
                        }

                        if (hasItems) {
                            for (ItemStack tradeItem : tradeItems) {
                                int removed = 0;
                                int needed = tradeItem.getAmount();
                                for (ItemStack item : pinv.getContents())
                                    if (removed < needed)
                                        if (item != null && item.isSimilar(tradeItem)) {
                                            removed += item.getAmount();
                                            if (removed >= needed)
                                                ItemUtils.removeItem(item,
                                                        item.getAmount() - (removed - needed));
                                            else
                                                ItemUtils.removeItem(item, item.getAmount());
                                    }
                            }

                            for (int i = 0; i < amt; i++)
                                for (ItemStack tradeItem : tradeItems)
                                    ItemUtils.addItemSafe(p, tradeItem);

                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&aYou bought &3" + amt + "x&6 " + dName));
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cYou don't have the required items!"));
                            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                        }
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