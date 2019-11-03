package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.LeaderboardManager;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class SortLeaderboards {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.admin")) {
            LeaderboardManager lm = UniversalCoreRemake.getLeaderboardManager();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aSorting leaderboards..."));
            long startTime = System.nanoTime();

            lm.manuallySortData();

            long endTime = System.nanoTime();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&eSorted leaderboards " +
                            "&a(took " + TextUtils.addCommas((int) ((endTime - startTime) / 1000000)) + "ms)"));
        } else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}