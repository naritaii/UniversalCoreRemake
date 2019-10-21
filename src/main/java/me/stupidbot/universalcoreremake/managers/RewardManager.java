package me.stupidbot.universalcoreremake.managers;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RewardManager implements Listener {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");

    public static SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    @EventHandler
    public void onVotifierEvent(VotifierEvent e) {
        Vote vote = e.getVote();
        Player p = Bukkit.getPlayer(vote.getUsername());

        if (p != null) {
            UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
            String oldTimestamp = up.getRewardTimestamp("Vote");
            String timestamp = simpleDateFormat.format(new Date());

            try {
                up.incrementTimesRewarded("Vote"); // More like "times voted" not "times rewarded"
                if (oldTimestamp == null || checkDaysBetween(oldTimestamp, timestamp) > 0) {
                    up.setRewardTimestamp("Vote", timestamp);
                    rewardVote(p);
                    if (checkDaysBetween(oldTimestamp, timestamp) > 2)
                        up.incrementStreak("Vote");
                    else
                        up.resetStreak("Vote");
                } else
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThank you " +
                            "for voting! However, we can only reward you for 1 vote per day, we appreciate it though :)!"));
            } catch (ParseException ex) { // this wont happen unless someone fucks something up. if you do fuck something up, just know you're now responsible for the implosion of the universe
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cThere was an error processing your vote, if this message ever appears then just " +
                                "know that this is &osomeone's&c fault, this error doesn't *just* happen, someone has " +
                                "messed something up if this message has appeared, hopefully, assuming the universe has " +
                                "NOT imploded, your next vote will work. Please try again tomorrow..."));
                up.setRewardTimestamp("vote", timestamp);
            }
        }
    }

    private void rewardVote(Player p) {

    }

    private long checkDaysBetween(String oldTimestamp, String newTimestamp) throws ParseException {
        Date old = simpleDateFormat.parse(oldTimestamp);
        Date newd = simpleDateFormat.parse(newTimestamp);
        long diff = old.getTime() - newd.getTime();

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}