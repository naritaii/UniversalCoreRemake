package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.guis.Buy;
import me.stupidbot.universalcoreremake.items.UniversalItem;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.SellItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

class OpenBlacksmith {
    private final List<SellItem> array;

    OpenBlacksmith() {
        array = Arrays.asList(
                new SellItem(UniversalItem.BROKEN_STICK,
                        ChatColor.stripColor(UniversalItem.BROKEN_STICK.getItemMeta().getDisplayName()),
                        new ItemBuilder(UniversalItem.CORRUPTED_SHARD).amount(1).build()),
                new SellItem(UniversalItem.CORRUPTED_SWORD,
                        ChatColor.stripColor(UniversalItem.CORRUPTED_SWORD.getItemMeta().getDisplayName()),
                        new ItemBuilder(UniversalItem.CORRUPTED_SHARD).amount(96).build(),
                        new ItemStack(Material.IRON_INGOT, 16),
                        new ItemBuilder(UniversalItem.CORRUPTED_BONE).amount(16).build()),
                new SellItem(UniversalItem.BONE_SWORD,
                        ChatColor.stripColor(UniversalItem.BONE_SWORD.getItemMeta().getDisplayName()),
                        new ItemBuilder(UniversalItem.CORRUPTED_SHARD).amount(32).build(),
                        new ItemBuilder(UniversalItem.CORRUPTED_BONE).amount(32).build(),
                        new ItemStack(Material.WOOD, 128)),
                new SellItem(UniversalItem.FIRE_WAND,
                        ChatColor.stripColor(UniversalItem.FIRE_WAND.getItemMeta().getDisplayName()),
                        new ItemBuilder(UniversalItem.INFESTED_FIRE).amount(5).build(),
                        new ItemStack(Material.IRON_INGOT, 128),
                        new ItemStack(Material.WOOD, 64),
                        new ItemBuilder(UniversalItem.CORRUPTED_BONE).amount(32).build()));
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