package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.guis.Sell;
import me.stupidbot.universalcoreremake.utilities.item.SellItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

class OpenMineralTrader {
    private final List<SellItem> array;

    OpenMineralTrader() {
        array = Arrays.asList(
                new SellItem(Material.RED_SANDSTONE, 0.70),
                new SellItem(Material.SANDSTONE, 2.00),
                new SellItem(Material.COAL_ORE, 4.20),
                new SellItem(Material.COAL, 5.55),
                new SellItem(Material.IRON_ORE, 5.25),
                new SellItem(Material.IRON_INGOT, 6.94));
    }

    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (s.hasPermission("universalcore.admin"))
            if (s instanceof Player)
                if (args.length == 0)
                    Sell.getInventory("Mineral Trader", array).open((Player) s);
                else
                    Sell.getInventory("Mineral Trader", array).open(Bukkit.getPlayer(args[0]));
            else if (args.length == 0)
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " <player>"));
            else
                Sell.getInventory("Mineral Trader", array).open(Bukkit.getPlayer(args[0]));
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}