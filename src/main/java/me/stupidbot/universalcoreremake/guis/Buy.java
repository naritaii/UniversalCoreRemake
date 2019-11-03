package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
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
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Buy implements InventoryProvider {
    private final List<SellItem> items;

    private Buy(List<SellItem> items) {
        this.items = items;
    }

    public static SmartInventory getInventory(String title, List<SellItem> items) {
        return SmartInventory.builder()
                .id(title.toLowerCase().replaceAll("\\s",""))
                .provider(new Buy(items))
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(6, 9)
                .title(title).build();
    }

    // --Commented out by Inspection (10/30/2019 3:00 PM):private final int page = 1;
    private final List<ClickableItem> clickableItems = new ArrayList<>();
    private final int amt = 1;

    @Override
    public void init(Player p, InventoryContents contents) {
        if (clickableItems.isEmpty())
            for (SellItem si : items) {
                double cost = si.getSellCost() * amt;
                Material m = si.getType();
                String mName = TextUtils.capitalizeFully(m.toString());
                String dName = (si.getDisplayItem().getItemMeta().hasDisplayName() ?
                        si.getDisplayItem().getItemMeta().getDisplayName() : mName);
                ItemStack icon = new ItemBuilder(si.getDisplayItem())
                        .amount(amt)
                        .name("&a" + dName)
                        .lore("")
                        .lore("&7Cost:")
                        .lore("&6$" + TextUtils.addCommas(cost))
                        .lore("")
                        .lore("&eClick to buy!").build();

                clickableItems.add(ClickableItem.of(icon, e -> {
                    Economy econ = UniversalCoreRemake.getEconomy();
                    if (econ.getBalance(p) > cost) {
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