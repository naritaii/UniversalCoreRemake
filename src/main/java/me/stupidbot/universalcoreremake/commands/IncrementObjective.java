package me.stupidbot.universalcoreremake.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class IncrementObjective {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (!s.hasPermission("universalcore.admin"))
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        else if (args.length == 0)
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cInvalid usage! /" + label + " <id> [amt] [player]"));
        else {
            Player p = null;
            if (args.length > 2) {
                p = Bukkit.getPlayer(args[2]);
                if (p == null)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cPlayer " + args[2] + " could not be found, /" + label + " does not yet " +
                                    "support offline players!")); // TODO Support this.
            } else if (s instanceof Player)
                p = (Player) s;
            else if (args.length > 1)
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " " + args[0] + " " + args[1] + " <player>"));
            else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + args[0] + " <amt> <player>"));

            if (p != null) {
                // TODO
            }
        }
        return true;
    }
}
