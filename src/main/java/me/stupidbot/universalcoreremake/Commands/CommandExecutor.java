package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    private CommandExecutor instance;

    private SetTrail setTrail = new SetTrail();

    public CommandExecutor(UniversalCoreRemake instance) { }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (args[0].equalsIgnoreCase("settrail"))
            return setTrail.execute(s, cmd, label, args);
        else
            return false;
        }
}