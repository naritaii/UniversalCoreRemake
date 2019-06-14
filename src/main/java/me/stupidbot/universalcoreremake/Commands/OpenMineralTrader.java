package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.GUI.MineralTrader;
import me.stupidbot.universalcoreremake.GUI.UniversalGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class OpenMineralTrader {
    private final UniversalGUI mineralTraderGui;

    OpenMineralTrader() {
        mineralTraderGui = new MineralTrader();
    }

    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (s.hasPermission("universalcore.admin"))
            if (s instanceof Player)
                if (args.length == 0)
                    mineralTraderGui.open((Player) s);
                else
                    mineralTraderGui.open(Bukkit.getPlayer(args[0]));
            else
                if (args.length == 0)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cInvalid usage! /" + label + " <player>"));
                else
                    mineralTraderGui.open(Bukkit.getPlayer(args[0]));
            else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}