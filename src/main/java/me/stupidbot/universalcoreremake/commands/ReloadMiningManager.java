package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.mining.MiningManager;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class ReloadMiningManager {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.admin")) {
            MiningManager mm = UniversalCoreRemake.getMiningManager();
            long startTime = System.nanoTime();

            mm.reload();

            long endTime = System.nanoTime();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&eReloaded mining manager " +
                            "&a(took " + TextUtils.addCommas((int) ((endTime - startTime) / 1000000)) + "ms)"));
        } else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}