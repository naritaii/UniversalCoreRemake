package me.stupidbot.universalcoreremake.Enchantments.Mutations;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Glass extends Enchantment implements Listener {
    private int id;
    public boolean hasListeners = true;

    public Glass(int id) {
        super(id);
        this.id = id;
    }

    @Override
    public int getMaxLevel() {
        return 1; // The maximum level
    }

    @Override
    public String getName() {
        return null; // The name
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true; // Can it enchant items?
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false; // Will it conflict? In this case, nope
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null; // You can define a target here, I just set it to null...
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getStartLevel() {
        return 1; // The start level of your enchantment
    }
}
