package me.stupidbot.universalcoreremake.managers;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.universalobjective.UniversalObjectiveCompleteEvent;
import me.stupidbot.universalcoreremake.events.universalobjective.UniversalObjectiveIncrementEvent;
import me.stupidbot.universalcoreremake.events.universalobjective.UniversalObjectiveStartEvent;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreboardManager implements Listener {
    public ScoreboardManager() {
        initialize();
    }

    private final List<String> animationFrames = Arrays.asList(
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS", // 10 seconds

            "&6&lC&e&lORRUPT PRISONS",
            "&f&lC&6&lO&e&lRRUPT PRISONS",
            "&f&lCO&6&lR&e&lRUPT PRISONS",
            "&f&lCOR&6&lR&e&lUPT PRISONS",
            "&f&lCORR&6&lU&e&lPT PRISONS",
            "&f&lCORRU&6&lP&e&lT PRISONS",
            "&f&lCORRUP&6&lT &e&lPRISONS",
            "&f&lCORRUPT&6&l P&e&lRISONS",
            "&f&lCORRUPT P&6&lR&e&lISONS",
            "&f&lCORRUPT PR&6&lI&e&lSONS",
            "&f&lCORRUPT PRI&6&lS&e&lONS",
            "&f&lCORRUPT PRIS&6&lO&e&lNS",
            "&f&lCORRUPT PRISO&6&lN&e&lS",
            "&f&lCORRUPT PRISON&6&lS", // 3.5 seconds

            "&f&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&f&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS",
            "&f&lCORRUPT PRISONS",
            "&e&lCORRUPT PRISONS" // 1.5 seconds
    );

    private void initialize() {
/*      breaks npcs, need to find workaround

        Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
        if (s.getObjective("health") != null)
            s.getObjective("health").unregister(); // Unregistering to clear cache
        Objective o = s.registerNewObjective("health", "health");
        o.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c\u2764"));
        o.setDisplaySlot(DisplaySlot.BELOW_NAME);*/

        for (Player p : Bukkit.getOnlinePlayers())
            setupBoard(p);

        // Animate scoreboard name
        new BukkitRunnable() {
            int frame = 0;

            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    BPlayerBoard board = Netherboard.instance().getBoard(p);
                    if (board != null) {
                        String finalFrame = ChatColor.translateAlternateColorCodes('&', animationFrames.get(frame));
                        if (!finalFrame.equals(board.getName()))
                            board.setName(finalFrame);
                    }
                }

                frame++;
                if (frame == animationFrames.size())
                    frame = 0;
            }
        }.runTaskTimer(UniversalCoreRemake.getInstance(), 0, 5);
    }

    private void setupBoard(Player p, ScoreboardFormat sf) {
        BPlayerBoard board = Netherboard.instance().getBoard(p);
        if (board != null) {
            String name = board.getName();
            board.delete();
            board = Netherboard.instance().createBoard(p, name);
        } else
            board = Netherboard.instance().createBoard(p, "");

        List<String> format = sf.getFormat();
        int score = format.size();
        for (String s : format)
            board.set(formatLine(s, p), --score);
    }

    private void setupBoard(Player p) {
        setupBoard(p, getFormat(p));
    }

    private String formatLine(String s, Player p) {
        String r = s.replace("%money%", TextUtils.addCommas(UniversalCoreRemake.getEconomy().getBalance(p)));

        if (s.contains("%objective_description%") || s.contains("%objective_progress%")) {
            UniversalObjective storyQuest = null;
            for (UniversalObjective uo : UniversalCoreRemake.getUniversalObjectiveManager().trackedObjectives.get(p.getUniqueId()))
                if (uo.getCategory() == UniversalObjective.Catagory.STORY_QUEST) {
                    storyQuest = uo;
                    break;
                }

            if (storyQuest != null)
                r = r.replace("%objective_description%", storyQuest.getDescription())
                        .replace("%objective_progress%", TextUtils.addCommas(storyQuest.getProgress(p)))
                        .replace("%objective_needed%", TextUtils.addCommas(Integer.parseInt(storyQuest.getTaskInfo()[1])));
            else
                r = r.replace("%objective_description%", "&cERROR (UniversalObjective NULL)")
                        .replace("%objective_progress%", "&cERROR (UniversalObjective NULL)")
                        .replace("%objective_needed%", "&cERROR (UniversalObjective NULL)");
        }

        return ChatColor.translateAlternateColorCodes('&', r);
    }

    private ScoreboardFormat getFormat(Player p) {
        UniversalObjective storyQuest = null;
        for (UniversalObjective uo : UniversalCoreRemake.getUniversalObjectiveManager().trackedObjectives.getOrDefault(p.getUniqueId(), new ArrayList<>()))
            if (uo.getCategory() == UniversalObjective.Catagory.STORY_QUEST) {
                storyQuest = uo;
                break;
            }

        if (storyQuest != null) {
            int needed = Integer.parseInt(storyQuest.getTaskInfo()[1]);
            if (needed > 1)
                return ScoreboardFormat.DEFAULT_WITH_OBJECTIVE;
            else
                return ScoreboardFormat.DEFAULT_WITH_OBJECTIVE_NO_COUNT;
        } else
            return ScoreboardFormat.DEFAULT;
    }

    @EventHandler
    public void OnUserBalanceUpdate(UserBalanceUpdateEvent e) {
        Player p = e.getPlayer();
        BPlayerBoard board = Netherboard.instance().getBoard(p);
        if (board != null) {
            ScoreboardFormat sf = getFormat(p);
            int walletLine = sf.getWalletLine();
            if (walletLine > -1)
                board.set(formatLine("Wallet: &6$", p) + TextUtils.addCommas(e.getNewBalance().doubleValue()), walletLine);
        }
    }

    @EventHandler
    public void OnObjectiveIncrement(UniversalObjectiveIncrementEvent e) {
        UniversalObjective uo = e.getUniversalObjective();
        if (uo.getCategory() == UniversalObjective.Catagory.STORY_QUEST ||
                uo.getCategory() == UniversalObjective.Catagory.CONTEXTUAL_QUEST) {
            Player p = e.getPlayer();
            BPlayerBoard board = Netherboard.instance().getBoard(p);
            if (board != null) {
                ScoreboardFormat sf = getFormat(p);
                int descLine = sf.getObjectiveLine();
                if (descLine > -1)
                    board.set(formatLine("&e" + uo.getDescription(), p), descLine);
                int countLine = sf.getObjectiveProgressLine();
                if (countLine > -1)
                    board.set(formatLine("&8(&b" + TextUtils.addCommas(e.getNewProgress()) + "&7/&b" +
                            TextUtils.addCommas(e.getNeeded()) + "&8)", p), countLine);
            }
        }
    }

    @EventHandler
    public void OnObjectiveComplete(UniversalObjectiveCompleteEvent e) {
        setupBoard(e.getPlayer());
    }

    @EventHandler
    public void OnObjectiveStart(UniversalObjectiveStartEvent e) {
        if (e.getUniversalObjective().getCategory() == UniversalObjective.Catagory.STORY_QUEST ||
                e.getUniversalObjective().getCategory() == UniversalObjective.Catagory.CONTEXTUAL_QUEST)
            setupBoard(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (Netherboard.instance().getBoard(p) == null)
            setupBoard(p);
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        BPlayerBoard board = Netherboard.instance().getBoard(p);
        board.delete();
    }

    enum ScoreboardFormat {
        DEFAULT(Arrays.asList(
                "Wallet: &6$%money%",
                "",
                "&ewww.corruptprisons.com"
        ), 2, -1, -1),
        DEFAULT_WITH_OBJECTIVE(Arrays.asList(
                "Wallet: &6$%money%",
                "&0",  // Can't have duplicate lines so invisible color codes
                "Objective:",
                "&e%objective_description%",
                "&8(&b%objective_progress%&7/&b%objective_needed%&8)",
                "",
                "&ewww.corruptprisons.com"
        ), 6, 3, 2),
        DEFAULT_WITH_OBJECTIVE_NO_COUNT(Arrays.asList(
                "Wallet: &6$%money%",
                "&0",
                "Objective:",
                "&e%objective_description%",
                "",
                "&ewww.corruptprisons.com"
        ), 5, 2, -1);
        private final List<String> format;
        private final int walletLine;
        private final int objectiveLine;
        private final int objectiveProgressLine;

        ScoreboardFormat(List<String> format, int walletLine, int objectiveLine, int objectiveProgressLine) {
            this.format = format;
            this.walletLine = walletLine;
            this.objectiveLine = objectiveLine;
            this.objectiveProgressLine = objectiveProgressLine;
        }

        List<String> getFormat() {
            return format;
        }

        int getWalletLine() {
            return walletLine;
        }

        int getObjectiveLine() {
            return objectiveLine;
        }

        int getObjectiveProgressLine() {
            return objectiveProgressLine;
        }
    }
}