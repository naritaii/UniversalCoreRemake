package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.RewardManager;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class Vote {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        RewardManager rm = UniversalCoreRemake.getRewardManager();
        if (s instanceof Player) {
            Player p = (Player) s;
            UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);

            boolean finalCanVote = false;
            try {
                finalCanVote = up.getRewardTimestamp("Vote") == null || rm.checkDaysBetween(up.getRewardTimestamp("Vote"),
                        RewardManager.getSimpleDateFormat().format(new Date())) > 0;
            } catch (ParseException pe) {
                pe.printStackTrace();
            }

            if (finalCanVote) {
                int voteSite = up.getTimesRewarded("Vote") + 1;
                int voteSize = rm.voteSiteDictionary.size();
                while (voteSite > voteSize)
                    voteSite -= voteSize;
                String siteName = rm.voteSiteDictionary.get(voteSite - 1);
                String url = rm.voteSite.get(siteName);
                StringBuilder rewardLore = new StringBuilder("\n&aStreak Reward: ");
                StringReward rewards = rm.votingRewards.get(up.getStreak("Vote"));
                int streak = up.getStreak("Vote");
                int maxStreak = rm.votingRewards.size();
                while (streak > maxStreak)
                    streak -= maxStreak;
                StringBuilder streakLore = new StringBuilder("&7Streak: ");
                for (int i = 1; i <= maxStreak; i++)
                    streakLore.append(i == streak + 1 ? ChatColor.GOLD : (i < streak + 1 ? ChatColor.YELLOW :
                            ChatColor.DARK_GRAY))
                            .append(TextUtils.toBallNumber(i));
                if (rewards != null) {
                    String[] asStrings = rewards.asStrings();
                    if (asStrings != null) {
                        for (String r : asStrings)
                            if (r != null)
                                rewardLore.append("\n  &8+").append(r);
                    } else
                        rewardLore.append("&8NONE");
                } else
                    rewardLore.append("&8NONE");

                BookUtil.openPlayer(p, BookUtil.writtenBook()
                        .author("Corrupt Prisons")
                        .title("Vote Link " + (voteSite + 1))
                        .pages(
                                new BookUtil.PageBuilder()
                                        .add(new TextComponent(TextUtils.centerMessage("&e&lVOTE", 56)))
                                        .newLine()
                                        .newLine()
                                        .add(new TextComponent("Vote for us daily for better and better loot!"))
                                        .newLine()
                                        .newLine()
                                        .newLine()
                                        .newLine()
                                        .newLine()
                                        .newLine()
                                        .add(BookUtil.TextBuilder.of(TextUtils.centerMessage("&6&l&nCLICK HERE", 56))
                                                .onClick(BookUtil.ClickAction.openUrl(url))
                                                .onHover(BookUtil.HoverAction.showText(
                                                        BookUtil.TextBuilder.of("Click to vote for us on " + siteName
                                                                + "\n\n" + ChatColor.translateAlternateColorCodes('&',
                                                                streakLore.toString() + rewardLore.toString()))
                                                                .color(ChatColor.YELLOW)
                                                                .build()))
                                                .build())
                                        .build()
                        )
                        .build());
            } else {
                BookUtil.BookBuilder book = BookUtil.writtenBook()
                        .author("Corrupt Prisons")
                        .title("Vote Links");

                List<BaseComponent[]> pages = new ArrayList<>();
                int line = 3;
                int linkNum = 1;
                BookUtil.PageBuilder pg = new BookUtil.PageBuilder()
                        .add(new TextComponent(TextUtils.centerMessage("&e&lVOTE LINKS", 56)))
                        .newLine()
                        .newLine();
                for (Map.Entry<String, String> ent : rm.voteSite.entrySet()) {
                    String site = ent.getKey();
                    String url = ent.getValue();

                    pg.add(BookUtil.TextBuilder.of("\u27A4 ")
                            .onClick(BookUtil.ClickAction.openUrl(url))
                            .onHover(BookUtil.HoverAction.showText(
                                    BookUtil.TextBuilder.of("Click to vote for us on " + site)
                                            .color(ChatColor.YELLOW)
                                            .build()))
                            .color(ChatColor.GREEN)
                            .build())
                            .add(BookUtil.TextBuilder.of(site)
                                    .onClick(BookUtil.ClickAction.openUrl(url))
                                    .onHover(BookUtil.HoverAction.showText(
                                            BookUtil.TextBuilder.of("Click to vote for us on " + site)
                                                    .color(ChatColor.YELLOW)
                                                    .build()))
                                    .color(ChatColor.GOLD)
                                    .build());

                    line++;
                    linkNum++;
                    if (line > 14 && rm.voteSiteDictionary.size() > linkNum) {
                        pages.add(pg.build());
                        line = 1;
                        pg = new BookUtil.PageBuilder();
                    } else
                        pg.newLine();
                }
                pages.add(pg.build());
                BookUtil.openPlayer(p, book.pages(pages).build());
            }
        } else
            rm.voteSite.forEach((String site, String url) -> s.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&a" + site + " &f-> &e" + url)));
        return true;
    }
}