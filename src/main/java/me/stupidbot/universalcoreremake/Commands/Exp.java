package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.PlayerLevelling;
import me.stupidbot.universalcoreremake.Utilities.Players.UniversalPlayer;
import me.stupidbot.universalcoreremake.Utilities.Players.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.Utilities.Text.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Exp {
    public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
        if (!s.hasPermission("universalcore.admin"))
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        else if (args.length == 0)
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cInvalid usage! /" + label + " <levelup|<amount>> [player]"));
        else if (args.length > 0) {
            Player p = null;
            if (args.length > 1) {
                p = Bukkit.getPlayer(args[1]);
                if (p == null)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cPlayer " + args[1] + " could not be found, /" + label + " does not yet " +
                                    "support offline players!"));
                } else if (s instanceof Player)
                    p = (Player) s;
                else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cInvalid usage! /" + label + " " + args[0] + " <player>"));

                if (p != null)
                    try {
                        UniversalPlayer up = UniversalPlayerManager.getUniversalPlayer(p);

                        int xp = args[0].equalsIgnoreCase("levelup") ?
                                PlayerLevelling.xpToNextLevel(up.getDataLevel()) - up.getDataXp()
                                : Integer.parseInt(args[0]);

                        PlayerLevelling.giveXp(p, xp);
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aGave " + TextUtils.addCommas(xp) + " exp to " + p.getName()));
                    } catch (NumberFormatException e) {
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&c" + args[0] + " is not a valid number!"));
                    }
        }

        return true;
    }
}