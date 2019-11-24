package me.stupidbot.universalcoreremake;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.stupidbot.universalcoreremake.managers.RewardManager;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.Warp;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Date;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the directory
 * {@code /plugins/PlaceholderAPI/expansions} on your server.
 * <br>
 * <br>If you create such a class inside your own plugin, you have to
 * register it manually in your plugins {@code onEnable()} by using
 * {@code new YourExpansionClass().register();}
 */
class UniversalCoreExpansion extends PlaceholderExpansion {
    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     *
     * @return always true since we do not have any dependencies.
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor() {
        return "CorruptedPrisonsDevTeam";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier() {
        return "universalcore";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion() {
        return "1.0";
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player
     * A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param identifier
     * A String containing the identifier/value.
     * @return Possibly-null String of the requested identifier.
     */
    private final UniversalPlayerManager upm = UniversalCoreRemake.getUniversalPlayerManager();

    @Override
    public String onRequest(OfflinePlayer p, String identifier) {
        switch (identifier) {
            case "level": // %universalcore_level%
                return upm.getUniversalPlayer(p).getLevel() + "";
            case "leveltag": // %universalcore_leveltag%
                return PlayerLevelling.levelTag(upm.getUniversalPlayer(p).getLevel());
            case "levelbar": // %universalcore_levelbar%
                UniversalPlayer up = upm.getUniversalPlayer(p);
                return ChatColor.translateAlternateColorCodes('&', "&7[" +
                        TextUtils.getProgressBar(up.getXp(),
                                PlayerLevelling.xpToNextLevel(up.getLevel()),
                                18,
                                "|",
                                "&a",/**/
                                "&8"
                        ) + "&7]");
            case "currentxp": // %universalcore_currentxp%
                return TextUtils.addCommas(upm.getUniversalPlayer(p).getXp());
            case "neededxp": // %universalcore_neededxp%
                return TextUtils.addCommas(PlayerLevelling.xpToNextLevel(upm.getUniversalPlayer(p).getLevel()));
            case "levelpercent": // %universalcore_levelpercent%
                UniversalPlayer up2 = upm.getUniversalPlayer(p);
                double d = 100d * ((double) up2.getXp() / (double) PlayerLevelling.xpToNextLevel(up2.getLevel()));
                return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP) + "%";
            case "currentachievements": // %universalcore_currentachievements%
                long amt = upm.getUniversalPlayer(p).getCompletedObjectives().stream().filter((uo) ->
                        UniversalCoreRemake.getUniversalObjectiveManager().registeredObjectivesDictionary.containsKey(uo) &&
                                UniversalCoreRemake.getUniversalObjectiveManager().registeredObjectives.get(
                                        UniversalCoreRemake.getUniversalObjectiveManager().registeredObjectivesDictionary.get(uo)).getCategory()
                                        == UniversalObjective.Catagory.ACHIEVEMENT).count();
                return (amt >= UniversalCoreRemake.getUniversalObjectiveManager().totalAchievements ? ChatColor.AQUA :
                        ChatColor.GREEN) + (amt + "");
            case "allachievements": // %universalcore_allachievements%
                return TextUtils.addCommas(UniversalCoreRemake.getUniversalObjectiveManager().totalAchievements);
            case "namecolor": // %universalcore_namecolor%
                return upm.getUniversalPlayer(p).getNameColor();
            case "chatcolor": // %universalcore_chatcolor%
                return TextUtils.getChatColor(p);
            case "reward": // %universalcore_reward%
                UniversalPlayer up3 = upm.getUniversalPlayer(p);
                RewardManager rm = UniversalCoreRemake.getRewardManager();
                try {
                    if (up3.getRewardTimestamp("Vote") == null || rm.checkDaysBetween(up3.getRewardTimestamp("Vote"),
                            RewardManager.getSimpleDateFormat().format(new Date())) > 0)
                        return "VOTE REWARD AVAILABLE";
                    else
                        return "";
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "";
                }
            case "spawnportal": // %universalcore_spawnportal%
                return Warp.getWarpFromId(upm.getUniversalPlayer(p).getSelectedWarpId()).getName();
            case "dj": // %universalcore_dj%
                return UniversalCoreRemake.getPermissions().playerHas("world", p, "universalcore.dj") ?
                        "(Fake)" :
                        "";
            case "balance": // %universalcore_balance%
                return TextUtils.formatValue((long) (upm.getUniversalPlayer(p).getBankedMoney() +
                                        UniversalCoreRemake.getEconomy().getBalance(p)));
            default:
                return null;
        }
        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
    }
}