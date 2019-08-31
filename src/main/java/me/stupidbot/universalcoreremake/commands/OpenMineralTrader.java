package me.stupidbot.universalcoreremake.commands;

import fr.minuskube.inv.SmartInventory;
import me.stupidbot.universalcoreremake.guis.Sell;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.SellItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

class OpenMineralTrader {
    private final SmartInventory mineralTraderGui;

    OpenMineralTrader() {
        mineralTraderGui = Sell.getInventory("Mineral Trader", Arrays.asList(
                new SellItem(Material.RED_SANDSTONE, 0.70),
                new SellItem(Material.SANDSTONE, 2.00),
                new SellItem(new ItemBuilder(Material.COAL_ORE).lore("&cCOMING SOON").build(), 0),
                new SellItem(new ItemBuilder(Material.COAL).lore("&cCOMING SOON").build(), 0)));
    }

    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (s.hasPermission("universalcore.admin"))
            if (s instanceof Player)
                if (args.length == 0)
                    mineralTraderGui.open((Player) s);
                else
                    mineralTraderGui.open(Bukkit.getPlayer(args[0]));
            else
                if (args.length == 0)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cInvalid usage! /" + label + " <player>"));
                else
                    mineralTraderGui.open(Bukkit.getPlayer(args[0]));
            else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}