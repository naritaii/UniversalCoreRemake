package me.stupidbot.universalcoreremake.Managers.UniversalObjectives;

import me.stupidbot.universalcoreremake.Events.UniversalBlockBreakEvent;
import me.stupidbot.universalcoreremake.Managers.UniversalPlayers.UniversalPlayer;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemBuilder;
import me.stupidbot.universalcoreremake.Utilities.StringReward;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class UniversalObjectiveManager implements Listener {
    private static List<UniversalObjective> registeredObjectives = new ArrayList<>();
    private static List<UniversalObjective> STORY_QUESTObjectives = new ArrayList<>();
    private static List<UniversalObjective> ACHIEVEMENTObjectives = new ArrayList<>();
    private static List<UniversalObjective> MINE_BLOCKObjectives = new ArrayList<>();
    private static List<UniversalObjective> TALK_TO_NPCObjectives = new ArrayList<>();

    public UniversalObjectiveManager() {
        instantiate();
    }

    private void instantiate() {
        registerObjectives();
    }

    private static final UniversalObjective storyGettingStarted =
        new UniversalObjective(
                UniversalObjective.TaskType.TALK_TO_NPC,
                new String[] {
                        "6594590a-d472-4f0f-bf7f-e4f42473522a", // Unique section ID, generated manually somehow
                        "1", // Integer to get too to complete
                        "328d73d1-e671-4006-8438-aeb44077b54f"  // NPC UUID
                },
                "getting_started", // Unique Objective ID
                new ItemBuilder(Material.YELLOW_FLOWER).name("&6Getting Started").build(),
                new StringReward("MONEY 1"),
                null,
                UniversalObjective.Catagory.STORY_QUEST
        );
    private static void registerObjectives() {
        registeredObjectives.forEach((UniversalObjective::saveData));
        registeredObjectives = new ArrayList<>();

        STORY_QUESTObjectives = new ArrayList<>();
        ACHIEVEMENTObjectives = new ArrayList<>();

        MINE_BLOCKObjectives = new ArrayList<>();
        TALK_TO_NPCObjectives = new ArrayList<>();


        registeredObjectives.add(storyGettingStarted);


        registeredObjectives.forEach((uo) -> {
            switch (uo.getCategory()) {
            case STORY_QUEST:
                STORY_QUESTObjectives.add(uo);
                break;
            case ACHIEVEMENT:
                ACHIEVEMENTObjectives.add(uo);
                break;
            }
            switch (uo.getTask()) {
                case MINE_BLOCK:
                    MINE_BLOCKObjectives.add(uo);
                    break;
                case TALK_TO_NPC:
                    TALK_TO_NPCObjectives.add(uo);
                    break;
            }
        });
    }

    public void disable() {
        registeredObjectives.forEach((UniversalObjective::saveData));
    }

    /**
     *           Increments by {@param amt} if {@param task} and {@param targetInfo}
     *           are equal to that of a {@link UniversalObjective} tracking {@param p}
     */
    private void increment(UniversalObjective.TaskType task, String taskInfo, Player p, int amt) {
        registeredObjectives.forEach((UniversalObjective uo) -> {
                if (uo.getTask() == task &&
                        uo.getPlayersTracking().contains(p.getUniqueId()) &&
                        uo.getTaskInfo()[2].equals(taskInfo)) { // TODO Use a dictionary map instead
                    int progress = uo.increment(p, amt);
                    if (progress >= Integer.parseInt(uo.getTaskInfo()[1]))
                        reward(p, uo);
                }
        });
    }


    private void reward(Player p, UniversalObjective uo) {
        StringReward rewards = uo.getRewards();
        UniversalObjective.Catagory type = uo.getCategory();

        uo.removePlayer(p);
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        up.addCompletedObjective(uo.getId());
        up.removeSelectedObjective(uo.getId()); // If objective was manually selected
        up.removeObjectiveData(uo.getId()); // No longer needed data

        p.sendMessage("");
        for (String s : rewards.asStrings()) {

        }

        rewards.giveNoMessage(p);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        List<String> completed = up.getCompletedObjectives();
        if (up.firstJoin())
            up.addSelectedObjective(storyGettingStarted.getId());
        List<String> selected = up.getSelectedObjectives();

        registeredObjectives.forEach((uo) -> {
            if (uo.getCategory() == UniversalObjective.Catagory.ACHIEVEMENT) {
                if (!completed.contains(uo.getId()))
                    uo.addPlayer(p);
            } else if (selected.contains(uo.getId()))
                uo.addPlayer(p);
        });
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e) {
        registeredObjectives.forEach((UniversalObjective uo) -> uo.removePlayer(e.getPlayer()));
    }

    // Objective listeners
    @EventHandler
    public void OnUniversalBlockBreak(UniversalBlockBreakEvent e) {
        Player p = e.getPlayer();
        int amt = e.getAmount();
        String b = e.getBlock().toString();
        increment(UniversalObjective.TaskType.MINE_BLOCK, b, p, amt);
    }

    @EventHandler
    public void OnNPCClick(NPCLeftClickEvent e) {
        Player p = e.getClicker();
        NPC npc = e.getNPC();
        String id = npc.getUniqueId().toString();
        increment(UniversalObjective.TaskType.TALK_TO_NPC, id, p, 1);
    }

    @EventHandler
    public void OnNPCClick(NPCRightClickEvent e) {
        Player p = e.getClicker();
        NPC npc = e.getNPC();
        String id = npc.getUniqueId().toString();
        increment(UniversalObjective.TaskType.TALK_TO_NPC, id, p, 1);
    }
}
