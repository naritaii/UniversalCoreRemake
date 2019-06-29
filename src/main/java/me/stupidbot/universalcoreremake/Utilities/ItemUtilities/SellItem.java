package me.stupidbot.universalcoreremake.Utilities.ItemUtilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SellItem {
    private final double sellCost;
    private final ItemStack item;
    private final ItemStack displayItem;

    public SellItem(Material item, double sellCost) {
        this.sellCost = sellCost;
        this.item = new ItemStack(item);
        this.displayItem = this.item;
    }

    public SellItem(ItemStack item, double sellCost) {
        this.sellCost = sellCost;
        this.item = item;
        this.displayItem = this.item;
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


    public double getSellCost() {
        return sellCost;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public ItemStack getDisplayItem() { return displayItem.clone(); }

    public Material getType() { return item.getType(); }
}