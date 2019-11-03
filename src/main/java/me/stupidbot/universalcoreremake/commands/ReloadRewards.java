package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.RewardManager;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class ReloadRewards {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.admin")) {
            RewardManager rm = UniversalCoreRemake.getRewardManager();
            long startTime = System.nanoTime();

            rm.reload();

            long endTime = System.nanoTime();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&eReloaded rewards " +
                            "&a(took " + TextUtils.addCommas((int) ((endTime - startTime) / 1000000)) + "ms)"));
        } else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}