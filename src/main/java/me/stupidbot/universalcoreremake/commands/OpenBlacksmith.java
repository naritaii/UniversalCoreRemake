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
                new SellItem(UniversalItem.RED_SANDSTONE_PICKAXE, "Red Sandstone Pickaxe",
                        UniversalItem.CORRUPTED_SHARD),
                new SellItem(new ItemBuilder(UniversalItem.BROKEN_STICK).lore("&7&oSeems overpriced...").build(),
                        ChatColor.stripColor(UniversalItem.BROKEN_STICK.getItemMeta().getDisplayName()), 50),
                new SellItem(UniversalItem.CORRUPTED_SWORD,
                        ChatColor.stripColor(UniversalItem.CORRUPTED_SWORD.getItemMeta().getDisplayName()),
                        new ItemBuilder(UniversalItem.CORRUPTED_SHARD).amount(96).build(),
                        new ItemStack(Material.IRON_INGOT, 16),
                        new ItemBuilder(UniversalItem.CORRUPTED_BONE).amount(16).build()),
                new SellItem(UniversalItem.LEATHER_HELMET,
                        ChatColor.stripColor(UniversalItem.LEATHER_HELMET.getItemMeta().getDisplayName()),
                        new ItemStack(Material.LEATHER, 16)),
                new SellItem(UniversalItem.LEATHER_CHESTPLATE,
                        ChatColor.stripColor(UniversalItem.LEATHER_CHESTPLATE.getItemMeta().getDisplayName()),
                        new ItemStack(Material.LEATHER, 32),
                        new ItemStack(Material.RED_SANDSTONE, 32)),
                new SellItem(UniversalItem.LEATHER_LEGGINGS,
                        ChatColor.stripColor(UniversalItem.LEATHER_LEGGINGS.getItemMeta().getDisplayName()),
                        new ItemStack(Material.LEATHER, 24)),
                new SellItem(UniversalItem.LEATHER_BOOTS,
                        ChatColor.stripColor(UniversalItem.LEATHER_BOOTS.getItemMeta().getDisplayName()),
                        new ItemStack(Material.LEATHER, 16),
                        new ItemStack(Material.STRING, 24)),
                new SellItem(UniversalItem.COAL_ORE_PICKAXE, "Coal Ore Pickaxe",
                        new ItemStack(Material.COAL, 32),
                        new ItemStack(Material.WOOD, 32)),
                new SellItem(UniversalItem.IRON_ORE_PICKAXE, "Iron Ore Pickaxe",
                        new ItemStack(Material.IRON_INGOT, 32),
                        new ItemStack(Material.WOOD, 32)),
                new SellItem(UniversalItem.CORRUPTED_PICKAXE, "Corrupted Pickaxe",
                        new ItemStack(Material.IRON_INGOT, 32),
                        new ItemBuilder(UniversalItem.CORRUPTED_SHARD).amount(32).build()),
                new SellItem(UniversalItem.REINFORCED_IRON_AXE, "Reinforced Axe",
                        new ItemStack(Material.IRON_INGOT, 32),
                        new ItemStack(Material.WOOD, 64)),
                new SellItem(UniversalItem.REINFORCED_WOODEN_SWORD,
                        ChatColor.stripColor(UniversalItem.REINFORCED_WOODEN_SWORD.getItemMeta().getDisplayName()),
                        new ItemStack(Material.IRON_INGOT, 32),
                        new ItemStack(Material.WOOD, 192)),
                new SellItem(UniversalItem.BONE_SWORD,
                        ChatColor.stripColor(UniversalItem.BONE_SWORD.getItemMeta().getDisplayName()),
                        new ItemBuilder(UniversalItem.CORRUPTED_SHARD).amount(32).build(),
                        new ItemBuilder(UniversalItem.CORRUPTED_BONE).amount(32).build(),
                        new ItemStack(Material.WOOD, 128)),
                new SellItem(UniversalItem.WOOL_HELMET,
                        ChatColor.stripColor(UniversalItem.WOOL_HELMET.getItemMeta().getDisplayName()),
                        new ItemStack(Material.WOOL, 32)),
                new SellItem(UniversalItem.WOOL_CHESTPLATE,
                        ChatColor.stripColor(UniversalItem.WOOL_CHESTPLATE.getItemMeta().getDisplayName()),
                        new ItemStack(Material.WOOL, 64),
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.IRON_INGOT, 12)),
                new SellItem(UniversalItem.WOOL_LEGGINGS,
                        ChatColor.stripColor(UniversalItem.WOOL_LEGGINGS.getItemMeta().getDisplayName()),
                        new ItemStack(Material.WOOL, 32),
                        new ItemStack(Material.STRING, 16)),
                new SellItem(UniversalItem.WOOL_BOOTS,
                        ChatColor.stripColor(UniversalItem.WOOL_BOOTS.getItemMeta().getDisplayName()),
                        new ItemStack(Material.WOOL, 32),
                        new ItemStack(Material.STRING, 32)),
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