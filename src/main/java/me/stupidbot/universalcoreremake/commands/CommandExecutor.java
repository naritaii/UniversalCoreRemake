package me.stupidbot.universalcoreremake.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    private final ReloadMOTD reloadMOTD = new ReloadMOTD();
    private final ReloadUniversalObjectives reloadUniversalObjectives = new ReloadUniversalObjectives();
    private final ReloadRewards reloadRewards = new ReloadRewards();
    private final ReloadMiningManager reloadMiningManager = new ReloadMiningManager();
    private final ReloadLeaderboards reloadLeaderboards = new ReloadLeaderboards();
    private final ReinitializeLeaderboards reinitializeLeaderboards = new ReinitializeLeaderboards();
    private final SortLeaderboards sortLeaderboards = new SortLeaderboards();
    private final Exp exp = new Exp();
    private final SetBlockMeta setBlockMeta = new SetBlockMeta();
    private final SelectObjective selectObjective = new SelectObjective();
    private final ReadBlockMeta readBlockMeta = new ReadBlockMeta();
    private final SetItemMeta setItemMeta = new SetItemMeta();
    private final ReadItemMeta readItemMeta = new ReadItemMeta();
    private final Emoji emoji = new Emoji();
    private final Vote vote = new Vote();
    private final Discord discord = new Discord();
    private final Twitter twitter = new Twitter();
    private final OpenMineralTrader openMineralTrader = new OpenMineralTrader();
    private final OpenFoodTrader openFoodTrader = new OpenFoodTrader();
    private final OpenBlacksmith openBlacksmith =  new OpenBlacksmith();
    private final OpenStats openStats = new OpenStats();
    private final OpenQuestMaster openQuestMaster = new OpenQuestMaster();
    private final OpenSpawnPortal openSpawnPortal = new OpenSpawnPortal();
    private final SaveUniversalPlayerCacheToFile saveUniversalPlayerCacheToFile = new SaveUniversalPlayerCacheToFile();
    private final SaveBlockmetaCacheToFile saveBlockmetaCacheToFile = new SaveBlockmetaCacheToFile();
    private final SaveLeaderboards saveLeaderboards = new SaveLeaderboards();
    private final Mutate mutate = new Mutate();
    private final Serialize serialize = new Serialize();
    private final StringReward stringReward = new StringReward();
    private final Hat hat = new Hat();

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) { // TODO Tab completion
        if (cmd.getName().equalsIgnoreCase("reloadmotd"))
            return reloadMOTD.execute(s);
        else if (cmd.getName().equalsIgnoreCase("reloaduniversalobjectives"))
            return reloadUniversalObjectives.execute(s);
        else if (cmd.getName().equalsIgnoreCase("reloadrewards"))
            return reloadRewards.execute(s);
        else if (cmd.getName().equalsIgnoreCase("reloadminingmanager"))
            return reloadMiningManager.execute(s);
        else if (cmd.getName().equalsIgnoreCase("reloadleaderboards"))
            return reloadLeaderboards.execute(s);
        else if (cmd.getName().equalsIgnoreCase("reinitializeleaderboards"))
            return reinitializeLeaderboards.execute(s);
        else if (cmd.getName().equalsIgnoreCase("sortleaderboards"))
            return sortLeaderboards.execute(s);
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
        else if (cmd.getName().equalsIgnoreCase("vote"))
            return vote.execute(s);
        else if (cmd.getName().equalsIgnoreCase("discord"))
            return discord.execute(s);
        else if (cmd.getName().equalsIgnoreCase("twitter"))
            return twitter.execute(s);
        else if (cmd.getName().equalsIgnoreCase("openmineraltrader"))
            return openMineralTrader.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("openfoodtrader"))
            return openFoodTrader.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("openblacksmith"))
            return openBlacksmith.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("openstats"))
            return openStats.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("openquestmaster"))
            return openQuestMaster.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("openspawnportal"))
            return openSpawnPortal.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("saveuniversalplayercachetofile"))
            return saveUniversalPlayerCacheToFile.execute(s);
        else if (cmd.getName().equalsIgnoreCase("saveblockmetadatacachetofile"))
            return saveBlockmetaCacheToFile.execute(s);
        else if (cmd.getName().equalsIgnoreCase("saveleaderboards"))
            return saveLeaderboards.execute(s);
        else if (cmd.getName().equalsIgnoreCase("mutate"))
            return mutate.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("serialize"))
            return serialize.execute(s);
        else if (cmd.getName().equalsIgnoreCase("stringreward"))
            return stringReward.execute(s, label, args);
        else if (cmd.getName().equalsIgnoreCase("hat"))
            return hat.execute(s);
        return false;
    }
}