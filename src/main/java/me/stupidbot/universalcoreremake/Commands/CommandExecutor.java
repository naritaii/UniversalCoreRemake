package me.stupidbot.universalcoreremake.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    private final ReloadMOTD reloadMOTD = new ReloadMOTD();
    private final Exp exp = new Exp();
    private final SetBlockMeta setBlockMeta = new SetBlockMeta();
    private final ReadBlockMeta readBlockMeta = new ReadBlockMeta();
    private final SetItemMeta setItemMeta = new SetItemMeta();
    private final ReadItemMeta readItemMeta = new ReadItemMeta();
    private final Emoji emoji = new Emoji();
    private final OpenMineralTrader openMineralTrader = new OpenMineralTrader();
    private final OpenFoodTrader openFoodTrader = new OpenFoodTrader();

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reloadmotd"))
            return reloadMOTD.execute(s);
        else if (cmd.getName().equalsIgnoreCase("exp"))
            return exp.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("setblockmeta"))
            return setBlockMeta.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("readblockmeta"))
            return readBlockMeta.execute(s);
        else if (cmd.getName().equalsIgnoreCase("setitemmeta"))
            return setItemMeta.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("readitemmeta"))
            return readItemMeta.execute(s);
        else if (cmd.getName().equalsIgnoreCase("emoji"))
            return emoji.execute(s);
        else if (cmd.getName().equalsIgnoreCase("openmineraltrader"))
            return openMineralTrader.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("openfoodtrader"))
            return openFoodTrader.execute(s, label, args);
        else
            return false;
        }
}