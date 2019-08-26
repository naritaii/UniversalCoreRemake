package me.stupidbot.universalcoreremake.commands;

import fr.minuskube.inv.SmartInventory;
import me.stupidbot.universalcoreremake.gui.Buy;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.SellItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

class OpenFoodTrader {
    private final SmartInventory foodTraderGui;

    OpenFoodTrader() {
        foodTraderGui = Buy.getInventory("Food Trader", Arrays.asList(
                new SellItem(new ItemBuilder(Material.ROTTEN_FLESH).name("&rOffal").lore("&3Stamina &a+5").build(), "Offal",
                        3.50),
                new SellItem(new ItemBuilder(Material.APPLE).name("&rPanacea").lore("&3Stamina &a+20").build(), "Panacea",
                        14.25)));
    }

    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (s.hasPermission("universalcore.admin"))
            if (s instanceof Player)
                if (args.length == 0)
                    foodTraderGui.open((Player) s);
                else
                    foodTraderGui.open(Bukkit.getPlayer(args[0]));
            else
                if (args.length == 0)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cInvalid usage! /" + label + " <player>"));
                else
                    foodTraderGui.open(Bukkit.getPlayer(args[0]));
            else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}