package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.managers.BlockMetadataManger;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class SaveBlockmetaCacheToFile {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.admin")) {
            BlockMetadataManger bmm = new BlockMetadataManger();
            int blocksSaved = bmm.getBlocksMetas().keySet().size();
            long startTime = System.nanoTime();

            bmm.save();

            long endTime = System.nanoTime();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&eSaved " +
                            "&a" + TextUtils.addCommas(blocksSaved) + "&e " +
                            "cached block metadata(s) to file " +
                            "&a(took " + TextUtils.addCommas((int) ((endTime - startTime) / 1000000)) + "ms)"));
        } else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}