package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.Managers.MOTDManager;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class ReloadMOTD {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.admin")) {
            MOTDManager mm = UniversalCoreRemake.getMotdManager();
            mm.reload();
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aReloaded MOTD.\n" + mm.motd.trim()));
        } else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}