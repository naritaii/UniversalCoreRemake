package me.stupidbot.universalcoreremake.enchantments.mutation;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class KillerOfDeath extends Enchantment implements Listener {
    private final int id;

    public KillerOfDeath(int id) {
        super(id);
        this.id = id;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "Killer Of Death";
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
        return EnchantmentTarget.WEAPON;
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
    public void OnDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player)
            if (((Player) e.getEntity()).getItemInHand().containsEnchantment(this))
                if (e.getEntity() instanceof Skeleton || e.getEntity() instanceof WitherSkeleton)
                    e.setDamage(e.getDamage() * 1.4d);
    }
}
