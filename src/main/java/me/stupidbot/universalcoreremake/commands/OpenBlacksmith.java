package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.guis.Buy;
import me.stupidbot.universalcoreremake.items.UniversalItem;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.SellItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

class OpenBlacksmith {
    private final List<SellItem> array;

    OpenBlacksmith() {
        array = Arrays.asList(
                new SellItem(UniversalItem.BROKEN_STICK,"Broken Stick",
                        new ItemBuilder(new ItemStack(UniversalItem.CORRUPTED_SHARD.getType()))
                                .name(UniversalItem.CORRUPTED_SHARD.getItemMeta().getDisplayName()).build()));
    }

    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (s.hasPermission("universalcore.admin"))
            if (s instanceof Player)
                if (args.length == 0)
                    Buy.getInventory("Blacksmith", array).open((Player) s);
                else
                    Buy.getInventory("Blacksmith", array).open(Bukkit.getPlayer(args[0]));
            else if (args.length == 0)
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " <player>"));
            else
                Buy.getInventory("Blacksmith", array).open(Bukkit.getPlayer(args[0]));
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}