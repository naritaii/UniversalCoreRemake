package me.stupidbot.universalcoreremake.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    private final ReloadMOTD reloadMOTD = new ReloadMOTD();
    private final ReloadUniversalObjectives reloadUniversalObjectives = new ReloadUniversalObjectives();
    private final Exp exp = new Exp();
    private final SetBlockMeta setBlockMeta = new SetBlockMeta();
    private final SelectObjective selectObjective = new SelectObjective();
    private final ReadBlockMeta readBlockMeta = new ReadBlockMeta();
    private final SetItemMeta setItemMeta = new SetItemMeta();
    private final ReadItemMeta readItemMeta = new ReadItemMeta();
    private final Emoji emoji = new Emoji();
    private final OpenMineralTrader openMineralTrader = new OpenMineralTrader();
    private final OpenFoodTrader openFoodTrader = new OpenFoodTrader();
    private final OpenStats openStats = new OpenStats();
    private final SaveUniversalPlayerCacheToFile saveUniversalPlayerCacheToFile = new SaveUniversalPlayerCacheToFile();
    private final SaveBlockmetaCacheToFile saveBlockmetaCacheToFile = new SaveBlockmetaCacheToFile();
    private final Mutate mutate = new Mutate();

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reloadmotd"))
            return reloadMOTD.execute(s);
        else if (cmd.getName().equalsIgnoreCase("reloaduniversalobjectives"))
            return reloadUniversalObjectives.execute(s);
        else if (cmd.getName().equalsIgnoreCase("exp"))
            return exp.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("setblockmeta"))
            return setBlockMeta.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("selectobjective"))
            return selectObjective.execute(s, label, args);
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
        else if (cmd.getName().equalsIgnoreCase("openstats"))
            return openStats.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("saveuniversalplayercachetofile"))
            return saveUniversalPlayerCacheToFile.execute(s);
        else if (cmd.getName().equalsIgnoreCase("saveblockmetadatacachetofile"))
            return saveBlockmetaCacheToFile.execute(s);
        else if (cmd.getName().equalsIgnoreCase("mutate"))
            return mutate.execute(s, label, args);

        else
            return false;
        }
}