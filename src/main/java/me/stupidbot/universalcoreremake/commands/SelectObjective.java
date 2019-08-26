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

class SelectObjective {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (!s.hasPermission("universalcore.admin"))
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        else if (args.length == 0)
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cInvalid usage! /" + label + " <id> [<player> [force]]"));
        else {
            Player p = null;
            if (args.length > 2) {
                p = Bukkit.getPlayer(args[1]);
                if (p == null)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cPlayer " + args[1] + " could not be found, /" + label + " does not yet " +
                                    "support offline players!")); // TODO Support this.
            } else if (s instanceof Player)
                p = (Player) s;
            else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " " + args[0] + " <player> [force]"));

            if (p != null) {
                UniversalObjectiveManager ubm = UniversalCoreRemake.getUniversalObjectiveManager();
                if (ubm.registeredObjectivesDictionary.containsKey(args[0])) {
                    UniversalObjective uo = ubm.registeredObjectives.get(ubm.registeredObjectivesDictionary.get(args[0]));
                    UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
                    String id = uo.getId();
                    boolean force = args.length > 2 && (args[2].equalsIgnoreCase("true") ||
                            args[2].equalsIgnoreCase("force"));

                    if (up.getSelectedObjectives().contains(id) || up.getCompletedObjectives().contains(id))
                        if (force) {
                            uo.removePlayer(p);
                            up.removeCompletedObjective(id);
                            up.removeObjectiveData(id);
                            uo.addPlayer(p);
                            up.addSelectedObjective(id);
                            UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, UniversalCoreRemake.getUniversalObjectiveManager().getNeeded(uo));
                            Bukkit.getServer().getPluginManager().callEvent(event);
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&a" + p.getName() + " has forcibly selected " + id));
                        } else
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&c" + p.getName() + " has already started or completed " + id +
                                            ", to ignore this use /" + label + " " + id + " " + p.getName() + " true"));
                    else {
                        uo.addPlayer(p);
                        up.addSelectedObjective(id);
                        UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, UniversalCoreRemake.getUniversalObjectiveManager().getNeeded(uo));
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&a" + p.getName() + " has selected " + id));
                    }
                } else {
                    List<String> ids = UniversalCoreRemake.getUniversalObjectiveManager().registeredObjectives.stream()
                            .map(UniversalObjective::getId).collect(Collectors.toList());
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&c" + args[0] + " is not a valid objective ID! Try " +
                                    Joiner.on(", ").join(ids .subList(0, ids .size() - 1))
                                    .concat(", or ").concat(ids.get(ids .size() - 1))));
                }
            }
        }
        return true;
    }
}
