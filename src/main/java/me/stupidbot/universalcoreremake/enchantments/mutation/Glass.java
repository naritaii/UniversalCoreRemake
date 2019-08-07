package me.stupidbot.universalcoreremake.enchantments.mutation;

import me.stupidbot.universalcoreremake.events.UniversalBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Glass extends Enchantment implements Listener {
    private final int id;

    public Glass(int id) {
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

    @EventHandler
    public void OnUniversalBlockBreak(UniversalBlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack i = p.getItemInHand();
        if (i.containsEnchantment(this)) {

        }

        Bukkit.broadcastMessage("glass listener works");
    }
}
