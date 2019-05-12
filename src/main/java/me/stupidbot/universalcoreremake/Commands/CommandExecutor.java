package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    private UniversalCoreRemake instance;

    private final SetTrail setTrail = new SetTrail();
    private final SetBlockMeta setBlockMeta = new SetBlockMeta();

    public CommandExecutor() {
        UniversalCoreRemake instance = this.instance;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("settrail"))
            return setTrail.execute(s, cmd, label, args);
        else if (cmd.getName().equalsIgnoreCase("setblockmeta"))
            return setBlockMeta.execute(s, cmd, label, args);
        else
            return false;
        }
}