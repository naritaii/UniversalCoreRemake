package me.stupidbot.universalcoreremake.Managers.UniversalObjectives;

import me.stupidbot.universalcoreremake.Managers.UniversalPlayers.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.StringReward;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UniversalObjective {
    private final TaskType task;
    private final String[] taskInfo;
    private final String id;
    private final ItemStack displayItem;
    private final StringReward rewards;
    private final int cooldown;
    private final String permissionRequired;
    private final Catagory category;

    private Map<UUID, Integer> playersToTrack;

    public UniversalObjective(TaskType task, String[] taskInfo, String id, ItemStack displayItem, StringReward rewards,
                              String permissionRequired, Catagory catagory) {
        this.task = task;
        this.taskInfo = taskInfo;
        this.id = id;
        this.displayItem = displayItem;
        this.rewards = rewards;
        this.cooldown = 0; // Not repeatable
        this.permissionRequired = permissionRequired; // null if no permission required
        this.category = catagory;
        playersToTrack = new HashMap<>();
    }

    TaskType getTask() {
        return task;
    }

    String[] getTaskInfo() {
        return taskInfo;
    }

    String getId() {
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

    int getCooldown() {
        return cooldown;
    }

    boolean hasPermissionRequired() {
        return (permissionRequired != null);
    }

    String getPermissionRequired() {
        return permissionRequired;
    }

    Catagory getCategory() {
        return category;
    }

    int increment(Player p, int amt) {
        int progress = getProgress(p) + amt;
        playersToTrack.put(p.getUniqueId(), progress);
        return progress;
    }

    int getProgress(Player p) {
        return playersToTrack.getOrDefault(p.getUniqueId(), 0);
    }

    void addPlayer(Player p) {
        playersToTrack.put(p.getUniqueId(), UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p)
                .getQuestData(getId(), getTaskFormatted()));
    }

    void removePlayer(Player p) {
        savePlayerData(p);
        playersToTrack.remove(p.getUniqueId());
    }

    void saveData() {
        UniversalPlayerManager upm = UniversalCoreRemake.getUniversalPlayerManager();
        for (Map.Entry<UUID, Integer> e : playersToTrack.entrySet()) {
            int progress = e.getValue();
            if (progress > 0)
                upm.getUniversalPlayer(e.getKey()).setQuestData(getId(), getTaskFormatted(), progress);
        }
    }

    private void savePlayerData(Player p) {
        UUID id = p.getUniqueId();
        int progress = playersToTrack.get(id);
        if (progress > 0)
            UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p)
            .setQuestData(getId(), getTaskFormatted(), progress);
    }

    private String getTaskFormatted() {
        return WordUtils.capitalizeFully(getTask().toString(), new char[] { '_' } ).replaceAll("_", "");
    }

    public enum TaskType {
        MINE_BLOCK, TALK_TO_NPC;
    }

    public enum Catagory {
        STORY_QUEST, // Completed one at a time, counter starting from 0 indicating player which quest player is on.
        ACHIEVEMENT; // All online players who haven't completed these are tracked
    }
}
