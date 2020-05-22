package me.stupidbot.universalcoreremake.managers;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.FileUtils;
import me.stupidbot.universalcoreremake.utilities.RandomCollection;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RewardManager implements Listener {
    public List<StringReward> votingRewards;
    public List<String> voteSiteDictionary;
    public Map<String, String> voteSite;
    public Map<Integer, StringReward> levelRewards;
    public RandomCollection<StringReward> commonArtifactRewards;

    public RewardManager() {
        reload();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void reload() {
        votingRewards = new ArrayList<>();
        voteSiteDictionary = new ArrayList<>();
        voteSite = new HashMap<>();
        levelRewards = new HashMap<>();
        commonArtifactRewards = new RandomCollection<>();
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

        for (String s : c.getConfigurationSection("Rewards.Levelling.PlayerLevel").getKeys(false))
            levelRewards.put(Integer.parseInt(s),
                    new StringReward(c.getStringList("Rewards.Levelling.PlayerLevel." + s).toArray(new String[0])));

        for (String s : c.getConfigurationSection("Rewards.Artifact.Common").getKeys(false)) {
            String p = "Rewards.Artifact.Common." + s;
            Material material = (c.getString(p + ".DisplayItem.ItemMaterial") != null ?
                    Material.matchMaterial(c.getString(p + ".DisplayItem.ItemMaterial")) : Material.GLASS);
            ItemBuilder displayItem = new ItemBuilder(
                    new ItemStack(material,
                            Math.max(1, c.getInt(p + ".DisplayItem.Amount")),
                            (short) c.getInt(p + ".DisplayItem.ItemData")));

            displayItem.name("&a" + (c.getString(p + ".DisplayItem.DisplayName") == null ?
                    TextUtils.capitalizeFully(material.toString()) :
                    ChatColor.translateAlternateColorCodes('&',
                            c.getString(p + ".DisplayItem.DisplayName"))));


            commonArtifactRewards.add(
                    c.getDouble(p + ".Weight"), new StringReward(c.getStringList(p + ".StringReward")
                            .toArray(new String[0]), displayItem.build()));
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
                long days = checkDaysBetween(oldTimestamp, timestamp);
                if (oldTimestamp == null || days > 0) {
                    if (oldTimestamp == null || days < 2)
                        up.incrementStreak("Vote");
                    else {
                        up.resetStreak("Vote");
                        up.incrementStreak("Vote");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVoting" +
                                " streak reset! Vote every day to increase it!"));
                    }
                    up.setRewardTimestamp("Vote", timestamp);
                    up.incrementTimesRewarded("Vote");
                    rewardVote(p);
                    // TODO Vote effect
                } else
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThank you " +
                            "for voting! We can only reward you for 1 vote per day but we appreciate it :)!"));
            } catch (ParseException ex) { // this wont happen unless someone fucks something up. if you do fuck something up, just know you're now responsible for the implosion of the universe
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cThere was an error processing your vote, if this message ever appears then just " +
                                "know that this is &osomeone's&c fault, this error doesn't *just* happen, someone has " +
                                "messed something up if this message has appeared, hopefully, assuming the universe has " +
                                "NOT imploded, your next vote will work. Please try again tomorrow..."));
                up.setRewardTimestamp("Vote", timestamp);
            }
        }
    }

    @EventHandler
    public void OnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        String oldTimestamp = up.getRewardTimestamp("Vote");
        if (oldTimestamp != null)
            if (up.getStreak("Vote") > 0)
                try {
                    long days = checkDaysBetween(oldTimestamp, simpleDateFormat.format(new Date()));
                    if (days > 1) {
                        up.resetStreak("Vote");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVoting" +
                                " streak reset! Vote every day to increase it!"));
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
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
        long diff = newd.getTime() - old.getTime();

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}