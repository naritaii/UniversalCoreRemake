package me.stupidbot.universalcoreremake.managers;

import org.bukkit.event.Listener;

public class ScoreboardManager implements Listener {
    public ScoreboardManager() {
        initialize();
    }

    private void initialize() {
/*        Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
        if (s.getObjective("health") != null)
            s.getObjective("health").unregister(); // Unregistering to clear cache
        Objective o = s.registerNewObjective("health", "health");
        o.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c\u2764"));
        o.setDisplaySlot(DisplaySlot.BELOW_NAME); breaks npcs, need to find workaround*/
    }
}
