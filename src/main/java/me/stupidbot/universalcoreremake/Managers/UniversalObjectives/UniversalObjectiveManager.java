package me.stupidbot.universalcoreremake.Managers.UniversalObjectives;

import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemBuilder;
import me.stupidbot.universalcoreremake.Utilities.StringReward;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class UniversalObjectiveManager implements Listener {
    private List<UniversalObjective> registeredObjectives;

    public UniversalObjectiveManager() {
        instantiate();
    }

    private void instantiate() {
        registerObjectives();
    }

    private void registerObjectives() {
        if (registeredObjectives != null)
            registeredObjectives.forEach((UniversalObjective::saveData));
        registeredObjectives = new ArrayList<>();

        registeredObjectives.add(new UniversalObjective(
                UniversalObjective.TaskType.TALK_TO_NPC,
                new String[] {
                        "6594590a-d472-4f0f-bf7f-e4f42473522a", // Unique Objective ID, generated manually somehow
                        "1", // Integer to get too to complete
                        "328d73d1-e671-4006-8438-aeb44077b54f"  // NPC UUID
                },
                "GettingStarted",
                new ItemBuilder(Material.YELLOW_FLOWER).name("&6Getting Started").build(),
                new StringReward("MONEY 1"),
                null,
                UniversalObjective.Catagory.STORY_QUEST
        ));
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
            if (uo.getTask() == task)
                if (uo.getTaskInfo()[2].equals(taskInfo)) {
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

        switch (type) {
            case STORY_QUEST:
                break;

            case ACHIEVEMENT:
                break;
        }
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e) {

    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent e) {
        registeredObjectives.forEach((UniversalObjective uo) -> uo.removePlayer(e.getPlayer()));
    }

    @EventHandler
    public void OnNPCClick(NPCClickEvent e) {
        Player p = e.getClicker();
        NPC npc = e.getNPC();
        String id = npc.getUniqueId().toString();
        increment(UniversalObjective.TaskType.TALK_TO_NPC, id, p, 1);
    }
}
