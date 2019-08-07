package me.stupidbot.universalcoreremake.managers.universalobjective;

import com.google.common.base.Joiner;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayerManager;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
        this.description = description != null ? description : generateDescription(); // if null we'll generate it through code
        playersToTrack = new HashMap<>();
    }

    private String generateDescription() {
        switch(getTask()) {
            case MINE_BLOCK:
                List<String> originalList = Arrays.asList(getTaskInfo()[2].split(","));
                originalList.forEach(TextUtils::capitalizeFully);
                return "Mine " + TextUtils.addCommas(Integer.parseInt(getTaskInfo()[1])) + " "
                        + Joiner.on(", ").join(originalList.subList(0, originalList.size() - 1))
                        .concat(", or ").concat(originalList.get(originalList.size() - 1));
            case TALK_TO_NPC:
                return "Talk to " + CitizensAPI.getNPCRegistry()
                        .getByUniqueIdGlobal(UUID.fromString(getTaskInfo()[2])).getFullName();
        }
        return "Unable to generate description for " + getId();
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

    private int getCooldown() {
        return cooldown;
    }

    String getDescription() {
        return description;
    }

    Catagory getCategory() {
        return category;
    }

    Set<UUID> getPlayersTracking() {
        return playersToTrack.keySet();
    }

    int increment(Player p, int amt) {
        int progress = getProgress(p) + amt;
        playersToTrack.put(p.getUniqueId(), progress);
        return progress;
    }

    private int getProgress(Player p) {
        return playersToTrack.getOrDefault(p.getUniqueId(), 0);
    }

    void addPlayer(Player p) {
        playersToTrack.put(p.getUniqueId(), UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p)
                .getObjectiveData(getId(), getTaskFormatted()));
    }

    void removePlayer(Player p) {
        if (playersToTrack.containsKey(p.getUniqueId())) {
            savePlayerData(p);
            playersToTrack.remove(p.getUniqueId());
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
        ACHIEVEMENT // All online players who haven't completed these are tracked
    }
}
