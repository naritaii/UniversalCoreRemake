package me.stupidbot.universalcoreremake.commands;

import com.google.common.base.Joiner;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjectiveManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

class ResetObjective {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (!s.hasPermission("universalcore.admin"))
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        else if (args.length == 0)
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cInvalid usage! /" + label + " <id|all|selected|completed> [player]"));
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
                UniversalObjectiveManager ubm = UniversalCoreRemake.getUniversalObjectiveManager();
                if (ubm.registeredObjectivesDictionary.containsKey(args[0])) {

                    s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aObjective reset."));
                } else {
                    boolean executed = false;
                    if (args[0].equalsIgnoreCase("selected") || args[0].equalsIgnoreCase("all")) {

                        executed = true;
                    }

                    if (args[0].equalsIgnoreCase("completed") || args[0].equalsIgnoreCase("all")) {
                        executed = true;
                    }

                    if (!executed) {
                        List<String> ids = UniversalCoreRemake.getUniversalObjectiveManager().registeredObjectives.stream()
                                .map(UniversalObjective::getId).collect(Collectors.toList());
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&c" + args[0] + " is not a valid objective ID! Try " +
                                        Joiner.on(", ").join(ids.subList(0, ids.size() - 1))
                                                .concat(", or ").concat(ids.get(ids.size() - 1))));
                    } else
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aObjective(s) reset."));
                }
                // TODO
            }
        }
        return true;
    }
}
