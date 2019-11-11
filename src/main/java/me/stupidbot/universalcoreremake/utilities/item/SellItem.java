package me.stupidbot.universalcoreremake.utilities.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class SellItem {
    private final double sellCost;
    private final ItemStack item;
    private final ItemStack displayItem;
    private final ItemStack[] tradeItems;

    public SellItem(Material item, double sellCost) {
        this.sellCost = sellCost;
        this.tradeItems = null;
        this.item = new ItemStack(item);
        this.displayItem = this.item;
    }

    public SellItem(ItemStack item, double sellCost) {
        this.sellCost = sellCost;
        this.tradeItems = null;
        this.item = item;
        this.displayItem = this.item;
    }

    public SellItem(ItemStack item, String displayName, double sellCost) {
        this.sellCost = sellCost;
        this.tradeItems = null;
        this.item = item;
        this.displayItem = new ItemBuilder(getItem()).name(displayName).build();
    }

/*    public SellItem(ItemStack item, ItemStack tradeItem) {
        this.tradeItems = new ItemStack[]{ tradeItem };
        this.item = item;
        this.displayItem = this.item;
    }    */

    public SellItem(ItemStack item, ItemStack... tradeItems) {
        this.sellCost = -1;
        this.tradeItems = tradeItems;
        this.item = item;
        this.displayItem = this.item;
    }

    public SellItem(ItemStack item, String displayName, ItemStack... tradeItems) {
        this.sellCost = -1;
        this.tradeItems = tradeItems;
        this.item = item;
        this.displayItem = new ItemBuilder(getItem()).name(displayName).build();
    }

//    public SellItem(Material item, ItemStack displayItem, double sellCost) {
//        this.sellCost = sellCost;
//        this.item = new ItemStack(item);
//        this.displayItem = displayItem;
//    }
//
//    public SellItem(ItemStack item, ItemStack displayItem, double sellCost) {
//        this.sellCost = sellCost;
//        this.item = item;
//        this.displayItem = displayItem;
//    }


    public boolean hasSellCost() {
        return sellCost != -1;
    }

    public double getSellCost() {
        return sellCost;
    }

    public boolean hasTradeItems() {
        return tradeItems != null;
    }

    public ItemStack[] getTradeItems() {
        return tradeItems;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public ItemStack getDisplayItem() { return displayItem.clone(); }

    public Material getType() { return item.getType(); }
}