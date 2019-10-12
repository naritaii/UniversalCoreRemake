package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class Serialize {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s instanceof Player)
            if (!s.hasPermission("universalcore.admin"))
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cYou don't have permission to use this command!"));
            else {
                Player p = (Player) s;
                ItemStack i = p.getItemInHand();

                if (i.getType() != Material.AIR) {
                    String asString = ItemUtils.serializeItemStack(i);
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aItem serialized! Copy-paste from console.\n&e" + asString));
                    System.out.println("Serialized item:\n" + asString);
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou're not holding any items!"));
            }
        else s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cOnly players may use this command!"));

        return true;
    }
}