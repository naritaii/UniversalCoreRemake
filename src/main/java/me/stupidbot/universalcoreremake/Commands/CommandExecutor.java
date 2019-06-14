package me.stupidbot.universalcoreremake.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
/*    private UniversalCoreRemake instance;*/

    private final Exp exp = new Exp();
    private final SetBlockMeta setBlockMeta = new SetBlockMeta();
    private final Emoji emoji = new Emoji();

//    public CommandExecutor() {
//        UniversalCoreRemake instance = this.instance;
//    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("exp"))
            return exp.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("setblockmeta"))
            return setBlockMeta.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("emoji"))
            return emoji.execute(s);
        else
            return false;
        }
}