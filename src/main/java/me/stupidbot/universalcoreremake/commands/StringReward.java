package me.stupidbot.universalcoreremake.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class StringReward {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (!s.hasPermission("universalcore.admin"))
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        else if (args.length == 0)
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cInvalid usage! /" + label + " <string_reward>"));
        else {
            StringBuilder value = new StringBuilder();
            for (int t = 0; t < args.length; t++) {
                if (t > 0)
                    value.append(" ");
                value.append(args[t]);
            }

            me.stupidbot.universalcoreremake.utilities.StringReward test
                    = new me.stupidbot.universalcoreremake.utilities.StringReward(value.toString());

            String asStrings = test.asStrings()[0]; // Should only be 1 line
            if (asStrings != null)
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aAs String:&r " + asStrings));
            else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&aAs String:&c&o null"));

            if (s instanceof Player) {
                Player p = (Player) s;
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&8---&cTesting giveNoMessages&8---"));
                test.giveNoMessage(p);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&8---&cTesting give&8---"));
                test.give(p);
            } else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cOnly players can test give or giveNoMessage"));
        }
        return true;
    }
}
