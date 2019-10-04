package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.guis.SpawnPortal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class OpenSpawnPortal {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (s.hasPermission("universalcore.admin"))
            if (s instanceof Player)
                if (args.length == 0)
                    SpawnPortal.getInventory((Player) s).open((Player) s);
                else
                    SpawnPortal.getInventory(Bukkit.getPlayer(args[0])).open(Bukkit.getPlayer(args[0]));
            else
                if (args.length == 0)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cInvalid usage! /" + label + " <player>"));
                else
                    SpawnPortal.getInventory(Bukkit.getPlayer(args[0])).open(Bukkit.getPlayer(args[0]));
            else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}