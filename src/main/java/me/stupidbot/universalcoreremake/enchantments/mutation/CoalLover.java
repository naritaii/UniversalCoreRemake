package me.stupidbot.universalcoreremake.enchantments.mutation;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class CoalLover extends Enchantment {
    private final int id;

    public CoalLover(int id) {
        super(id);
        this.id = id;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "Coal Lover";
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
        return EnchantmentTarget.TOOL;
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

    // Listener in MiningManager
}
