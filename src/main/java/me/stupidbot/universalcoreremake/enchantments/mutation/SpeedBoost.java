package me.stupidbot.universalcoreremake.enchantments.mutation;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedBoost extends Enchantment implements Listener {
    private final int id;

    public SpeedBoost(int id) {
        super(id);
        this.id = id;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "Speed Boost";
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
    public void OnPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            ItemStack i = p.getItemInHand();

            int staminaCost = 25;
            if (i.containsEnchantment(this))
                if (UniversalCoreRemake.getStatsManager().getStamina(p) >= staminaCost) {
                    UniversalCoreRemake.getStatsManager().removeStamina(p, staminaCost);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 0,
                            true, true), false);
                    TextUtils.sendActionbar(p, "&3Stamina: &c-" + staminaCost + " &bSpeed: &a+20% 30s");
                }
        }
    }
}
