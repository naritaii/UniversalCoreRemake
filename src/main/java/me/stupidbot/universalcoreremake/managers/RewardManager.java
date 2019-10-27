package me.stupidbot.universalcoreremake.managers;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.FileUtils;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RewardManager implements Listener {
    public List<StringReward> votingRewards;
    public List<String> voteSiteDictionary;
    public Map<String, String> voteSite;

    public RewardManager() {
        reload();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void reload() { // TODO Make file reloadable with command
        votingRewards = new ArrayList<>();
        voteSiteDictionary = new ArrayList<>();
        voteSite = new HashMap<>();
        UniversalCoreRemake instance = UniversalCoreRemake.getInstance();
        File path = instance.getDataFolder();
        File file = new File(path.toString() + File.separator + "rewards.yml");

        if (!path.exists())
            path.mkdirs();
        if (!file.exists())
            FileUtils.copy(instance.getResource("rewards.yml"), file);

        FileConfiguration c = YamlConfiguration.loadConfiguration(file);

        for (String s : c.getConfigurationSection("Rewards.Voting.Streak").getKeys(false))
            votingRewards.add(new StringReward(c.getStringList("Rewards.Voting.Streak." + s).toArray(new String[0])));

        for (String s : c.getConfigurationSection("Rewards.Voting.Links").getKeys(false)) {
            voteSite.put(s, c.getString("Rewards.Voting.Links." + s));
            voteSiteDictionary.add(s);
        }
    }

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
                if (oldTimestamp == null || checkDaysBetween(oldTimestamp, timestamp) > 0) {
                    if (oldTimestamp == null || checkDaysBetween(oldTimestamp, timestamp) > 2)
                        up.incrementStreak("Vote");
                    else {
                        up.resetStreak("Vote");
                        up.incrementStreak("Vote");
                    }
                    up.setRewardTimestamp("Vote", timestamp);
                    up.incrementTimesRewarded("Vote");
                    rewardVote(p);
                } else
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThank you " +
                            "for voting! We can only reward you for 1 vote per day but we appreciate it :)!"));
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
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        int streak = up.getStreak("Vote");
        while (streak > votingRewards.size())
            streak -= votingRewards.size();
        StringReward rewards = votingRewards.get(streak - 1);
        if (rewards != null) {
            String[] asStrings = rewards.asStrings();
            if (asStrings != null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThank you " +
                        "for voting! Here are your rewards:"));
                for (String s : asStrings)
                    if (s != null)
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8+" + s));
                rewards.giveNoMessage(p);
            } else
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThank you " +
                        "for voting!"));
        }
    }

    public long checkDaysBetween(String oldTimestamp, String newTimestamp) throws ParseException {
        Date old = simpleDateFormat.parse(oldTimestamp);
        Date newd = simpleDateFormat.parse(newTimestamp);
        long diff = old.getTime() - newd.getTime();

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}