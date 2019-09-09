package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class SaveUniversalPlayerCacheToFile {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.admin")) {
            UniversalPlayerManager upm = UniversalCoreRemake.getUniversalPlayerManager();
            int filesSaved = upm.getAllUniversalPlayers().size();
            long startTime = System.nanoTime();

            upm.saveAll();
            upm.manuallyRefreshCache();

            long endTime = System.nanoTime();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&eSaved " +
                            "&a" + TextUtils.addCommas(filesSaved) + "&e " +
                            "cached UniversalPlayer file(s) to file, currently caching &a"
                            + TextUtils.addCommas(upm.getAllUniversalPlayers().size()) +
                            "&e files(s) &a(took " + TextUtils.addCommas((int) ((endTime - startTime) / 1000000)) + "ms)"));
        } else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}