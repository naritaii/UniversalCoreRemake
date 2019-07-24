package me.stupidbot.universalcoreremake.Managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager implements Listener {
    public ScoreboardManager() {
        initialize();
    }

    private void initialize() {
        Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
        if (s.getObjective("health") != null)
            s.getObjective("health").unregister(); // Unregistering to clear cache
        Objective o = s.registerNewObjective("health", "health");
        o.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c\u2764"));
        o.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }
}
