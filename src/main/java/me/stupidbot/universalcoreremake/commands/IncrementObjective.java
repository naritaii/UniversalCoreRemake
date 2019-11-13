package me.stupidbot.universalcoreremake.commands;

import com.google.common.base.Joiner;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.universalobjective.UniversalObjectiveStartEvent;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjectiveManager;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

class IncrementObjective {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (!s.hasPermission("universalcore.admin"))
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        else if (args.length == 0)
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cInvalid usage! /" + label + " <id> [player]"));
        else {
            Player p = null;
            if (args.length > 1) {
                p = Bukkit.getPlayer(args[1]);
                if (p == null)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cPlayer " + args[1] + " could not be found, /" + label + " does not yet " +
                                    "support offline players!")); // TODO Support this.
            } else if (s instanceof Player)
                p = (Player) s;
            else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " " + args[0] + " <player>"));

            if (p != null) {
                // TODO
            }
        }
        return true;
    }
}
