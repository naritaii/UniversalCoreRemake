package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.Managers.UniversalObjectives.UniversalObjectiveManager;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class ReloadUniversalObjectives {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.admin")) {
            UniversalObjectiveManager om = UniversalCoreRemake.getUniversalObjectiveManager();
            long startTime = System.nanoTime();

            om.registerObjectives();

            long endTime = System.nanoTime();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&eLoaded, registered, and tracking " +
                            "&a" + TextUtils.addCommas(om.registeredObjectives.size()) + "&e " +
                            "Universal Objective(s) from file " +
                            "&a(took " + TextUtils.addCommas((int) ((endTime - startTime) / 1000000)) + "ms)"));
        } else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}