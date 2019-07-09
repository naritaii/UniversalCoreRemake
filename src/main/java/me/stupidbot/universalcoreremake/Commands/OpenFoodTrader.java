package me.stupidbot.universalcoreremake.Commands;

import fr.minuskube.inv.SmartInventory;
import me.stupidbot.universalcoreremake.GUI.Buy;
import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemBuilder;
import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.SellItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

class OpenFoodTrader {
    private final SmartInventory foodTraderGui;

    OpenFoodTrader() {
        foodTraderGui = Buy.getInventory("Food Trader", Collections.singletonList(
                new SellItem(new ItemBuilder(Material.ROTTEN_FLESH).name("Offal").build(), 3.50)));
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