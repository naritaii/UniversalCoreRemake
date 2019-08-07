package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Exp {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (!s.hasPermission("universalcore.admin"))
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        else if (args.length == 0)
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cInvalid usage! /" + label + " <levelup|<amount>> [player]"));
        else {
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
                        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

                        int xp = args[0].equalsIgnoreCase("levelup") ?
                                PlayerLevelling.xpToNextLevel(up.getLevel()) - up.getXp()
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