package me.stupidbot.universalcoreremake.utilities;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.LeaderboardManager;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static void safeDeposit(Player p, double v) {
        LeaderboardManager.safeDeposit.add(p.getUniqueId());
        UniversalCoreRemake.getEconomy().depositPlayer(p, v);
    }
}
