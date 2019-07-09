package me.stupidbot.universalcoreremake.GUI;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemBuilder;
import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemUtils;
import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.SellItem;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class Buy implements InventoryProvider {
    private final List<SellItem> items;

    private Buy(List<SellItem> items) {
        this.items = items;
    }

    public static SmartInventory getInventory(String title, List<SellItem> items) {
        return SmartInventory.builder()
                .id(UUID.randomUUID().toString())
                .provider(new Buy(items))
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(6, 9)
                .title(title).build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        ItemStack border = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
                .name(" ").build();
        contents.fillBorders(ClickableItem.empty(border));

        for (SellItem si : items) {
            double cost = si.getSellCost();
            Material m = si.getType();
            String mName = TextUtils.capitalizeFully(m.toString());
            ItemStack icon = new ItemBuilder(si.getDisplayItem())
                    .name("&a" + (si.getDisplayItem().getItemMeta().hasDisplayName() ?
                            si.getDisplayItem().getItemMeta().getDisplayName() : mName))
                    .lore("")
                    .lore("&7Cost:")
                    .lore("&6$" + TextUtils.addCommas(cost))
                    .lore("")
                    .lore("&eClick to buy!").build();

            contents.add(ClickableItem.of(icon, e -> {
                Economy econ = UniversalCoreRemake.getEconomy();
                if (econ.getBalance(p) > cost) {
                    UniversalCoreRemake.getEconomy().withdrawPlayer(p, cost);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aYou bought &6" + mName +
                                    "&a for &c$" + TextUtils.addCommas(cost)));
                    ItemUtils.addItemSafe(p, si.getItem());
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                } else
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou don't have enough money!"));
            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}