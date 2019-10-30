package me.stupidbot.universalcoreremake.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Glow extends Enchantment {
    private final int id;

    public Glow(int id) {
        super(id);
        this.id = id;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    // Does nothing
}
