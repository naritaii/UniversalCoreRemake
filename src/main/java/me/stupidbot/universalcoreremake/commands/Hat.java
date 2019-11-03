package me.stupidbot.universalcoreremake.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class Hat {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.hat"))
            if (s instanceof Player) {
                Player p = (Player) s;
                ItemStack i = p.getItemInHand();
                ItemStack helm = p.getInventory().getHelmet();

                if (i.getType() != Material.AIR) {
                    p.getInventory().setHelmet(i);
                    p.setItemInHand(helm);
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aEnjoy your new hat!"));
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou're not holding any items!"));
            } else s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cOnly players may use this command!"));
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}