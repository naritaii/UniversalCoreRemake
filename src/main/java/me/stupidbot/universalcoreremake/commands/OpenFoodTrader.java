package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.guis.Buy;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.SellItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

class OpenFoodTrader {
    private final List<SellItem> array;

    OpenFoodTrader() {
        array = Arrays.asList(
                new SellItem(new ItemBuilder(Material.ROTTEN_FLESH).name("&rOffal").lore("&3Stamina &a+5").build(), "Offal",
                        3.50),
                new SellItem(new ItemBuilder(Material.APPLE).name("&rPanacea").lore("&3Stamina &a+20").build(), "Panacea",
                        14),
                new SellItem(new ItemBuilder(Material.BREAD).name("&rJunie Cake").lore("&3Stamina &a+50").build(), "Junie Cake",
                        35));
    }

    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (s.hasPermission("universalcore.admin"))
            if (s instanceof Player)
                if (args.length == 0)
                    Buy.getInventory("Food Trader", array).open((Player) s);
                else
                    Buy.getInventory("Food Trader", array).open(Bukkit.getPlayer(args[0]));
            else if (args.length == 0)
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " <player>"));
            else
                Buy.getInventory("Food Trader", array).open(Bukkit.getPlayer(args[0]));
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}