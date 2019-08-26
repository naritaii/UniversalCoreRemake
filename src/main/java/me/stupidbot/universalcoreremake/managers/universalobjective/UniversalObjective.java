package me.stupidbot.universalcoreremake.managers.universalobjective;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UniversalObjective {
    private final TaskType task;
    private final String[] taskInfo;
    private final String id;
    private final ItemStack displayItem;
    private final StringReward rewards;
    private final int cooldown;
    private final String description;
    private final Catagory category;

    private final Map<UUID, Integer> playersToTrack;

    public UniversalObjective(TaskType task, String[] taskInfo, String id, ItemStack displayItem, StringReward rewards,
                              String description, Catagory catagory) {
        this.task = task;
        this.taskInfo = taskInfo;
        this.id = id;
        this.displayItem = displayItem;
        this.rewards = rewards;
        this.cooldown = 0; // Not repeatable // TODO Add repeatable objectives
        this.category = catagory;
        this.description = description;
        playersToTrack = new ConcurrentHashMap<>();
    }

    TaskType getTask() {
        return task;
    }

    public String[] getTaskInfo() {
        return taskInfo;
    }

    public String getId() {
        return id;
    }

    ItemStack getDisplayItem() {
        return displayItem.clone();
    }

    StringReward getRewards() {
        return rewards;
    }

    boolean hasCooldown() {
        return (getCooldown() > 0);
    }

    private int getCooldown() {
        return cooldown;
    }

    public String getDescription() {
        return description;
    }

    public Catagory getCategory() {
        return category;
    }

    public Set<UUID> getPlayersTracking() {
        return playersToTrack.keySet();
    }

    int increment(Player p, int amt) {
        int progress = getProgress(p) + amt;
        playersToTrack.put(p.getUniqueId(), progress);
        return progress;
    }

    public int getProgress(Player p) {
        return playersToTrack.getOrDefault(p.getUniqueId(), 0);
    }

    public void addPlayer(Player p) {
        UUID id = p.getUniqueId();
        playersToTrack.put(id, UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p)
                .getObjectiveData(getId(), getTaskFormatted()));
        UniversalObjectiveManager uom = UniversalCoreRemake.getUniversalObjectiveManager();
        List<UniversalObjective> list = uom.trackedObjectives.getOrDefault(id, Collections.synchronizedList(new ArrayList<>()));
        list.add(this);
        uom.trackedObjectives.put(id, list);
    }

    public void removePlayer(Player p) {
        UUID id = p.getUniqueId();
        if (playersToTrack.containsKey(id)) {
            savePlayerData(p);
            playersToTrack.remove(id);
            UniversalObjectiveManager uom = UniversalCoreRemake.getUniversalObjectiveManager();
            List<UniversalObjective> list = uom.trackedObjectives.get(id);
            list.remove(this);
            uom.trackedObjectives.put(id, list);
        }
    }

    void saveData() {
        UniversalPlayerManager upm = UniversalCoreRemake.getUniversalPlayerManager();
        for (Map.Entry<UUID, Integer> e : playersToTrack.entrySet()) {
            int progress = e.getValue();
            if (progress > 0)
                upm.getUniversalPlayer(e.getKey()).setObjectiveData(getId(), getTaskFormatted(), progress);
        }
    }

    private void savePlayerData(Player p) {
        UUID id = p.getUniqueId();
        int progress = playersToTrack.get(id);
        if (progress > 0)
            UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p)
            .setObjectiveData(getId(), getTaskFormatted(), progress);
    }

    private String getTaskFormatted() {
        return WordUtils.capitalizeFully(getTask().toString(), new char[] { '_' } ).replaceAll("_", "");
    }

    public enum TaskType {
        MINE_BLOCK,
        TALK_TO_NPC
    }

    public enum Catagory {
        STORY_QUEST, // Completed one at a time
        CONTEXTUAL_QUEST, // Quests given upon entering a region or talking to an NPC for example
        ACHIEVEMENT // All online players who haven't completed these are tracked
    }
}
