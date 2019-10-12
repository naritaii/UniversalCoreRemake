package me.stupidbot.universalcoreremake.enchantments.mutation;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.UniversalBlockBreakEvent;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Glass extends Enchantment implements Listener {
    private final int id;

    public Glass(int id) {
        super(id);
        this.id = id;
    }

    @Override
    public int getMaxLevel() {
        return 200;
    }

    @Override
    public String getName() {
        return "Glass";
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


    private final Random r = new Random();

    @EventHandler
    public void OnUniversalBlockBreak(UniversalBlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack i = p.getItemInHand();

        if (i.containsEnchantment(this)) {
            int level = i.getEnchantmentLevel(this);
            float breakChance = level * 0.05f;

            if (r.nextFloat() <= breakChance) {
                UniversalCoreRemake.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(UniversalCoreRemake.getInstance(), () -> {
                    ItemUtils.removeItem(i, 1);

                    p.playSound(p.getLocation(), Sound.GLASS, 1f, 1f);
                    String name = i.getItemMeta().hasDisplayName() ? i.getItemMeta().getDisplayName() : TextUtils.capitalizeFully(i.getType().toString());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYour &e" + name + "&c broke because of its " + getName() + " " +
                                    (level > 1 ? TextUtils.toRoman(level) + " " : "") + "mutation!"));
                });
            }
        }
    }
}
