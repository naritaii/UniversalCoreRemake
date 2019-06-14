package me.stupidbot.universalcoreremake.Utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SellItem {
    private final double sellCost;
    private final ItemStack item;

    public SellItem(Material item, double sellCost) {
        this.sellCost = sellCost;
        this.item = new ItemStack(item);
    }

    public SellItem(ItemStack item, double sellCost) {
        this.sellCost = sellCost;
        this.item = item;
    }

    public double getSellCost() {
        return this.sellCost;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
