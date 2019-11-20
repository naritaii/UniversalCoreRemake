package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.guis.Buy;
import me.stupidbot.universalcoreremake.managers.StatsManager;
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
                new SellItem(new ItemBuilder(Material.ROTTEN_FLESH).name("&rOffal").lore("&3Stamina &a+" +
                        StatsManager.BaseFoodStamina.ROTTEN_FLESH.getBaseFoodStamina()).build(), "Offal",
                        StatsManager.BaseFoodStamina.ROTTEN_FLESH.getBaseFoodStamina() * 0.70d),
                new SellItem(new ItemBuilder(Material.APPLE).name("&rPanacea").lore("&3Stamina &a+" +
                        StatsManager.BaseFoodStamina.APPLE.getBaseFoodStamina()).build(), "Panacea",
                        StatsManager.BaseFoodStamina.APPLE.getBaseFoodStamina() * 0.70d),
                new SellItem(new ItemBuilder(Material.BREAD).name("&rJunie Cake").lore("&3Stamina &a+" +
                        StatsManager.BaseFoodStamina.BREAD.getBaseFoodStamina()).build(), "Junie Cake",
                        StatsManager.BaseFoodStamina.BREAD.getBaseFoodStamina() * 0.70d),
                new SellItem(new ItemBuilder(Material.RAW_FISH).name("&rCod Dead").lore("&3Stamina &a+" +
                        StatsManager.BaseFoodStamina.RAW_FISH.getBaseFoodStamina()).build(), "Cod Dead",
                        StatsManager.BaseFoodStamina.RAW_FISH.getBaseFoodStamina() * 0.70d),
                new SellItem(new ItemBuilder(Material.MUSHROOM_SOUP).name("&rSouper Bowl Of Broth").lore("&3Stamina &a+" +
                        StatsManager.BaseFoodStamina.MUSHROOM_SOUP.getBaseFoodStamina()).build(), "Souper Bowl Of Broth",
                        StatsManager.BaseFoodStamina.MUSHROOM_SOUP.getBaseFoodStamina() * 0.70d),
                new SellItem(new ItemBuilder(Material.RAW_CHICKEN).name("&rEggonomics").lore("&3Stamina &a+" +
                        StatsManager.BaseFoodStamina.RAW_CHICKEN.getBaseFoodStamina()).build(), "Eggonomics",
                        StatsManager.BaseFoodStamina.RAW_CHICKEN.getBaseFoodStamina() * 0.70d));
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