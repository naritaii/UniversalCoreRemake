package me.stupidbot.universalcoreremake.Managers.UniversalObjectives;

import me.stupidbot.universalcoreremake.Utilities.StringReward;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class UniversalObjective {
    private final Map<TaskType, String> tasks;
    private final String id;
    private final ItemStack displayItem;
    private final StringReward rewards;
    private final int cooldown;
    private final String permissionRequired;
    private final Catagory category;

    public UniversalObjective(Map<TaskType, String> tasks, String id, ItemStack displayItem, StringReward rewards,
                              String permissionRequired, Catagory catagory) {
        this.tasks = tasks;
        this.id = id;
        this.displayItem = displayItem;
        this.rewards = rewards;
        this.cooldown = 0; // Not repeatable
        this.permissionRequired = permissionRequired; // null if no permission required
        this.category = catagory;
    }

    Map<TaskType, String> getTasks() {
        return tasks;
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

    public enum TaskType {
        MINE_BLOCK, TALK_TO_NPC;
    }

    public enum Catagory {
        STORY_QUEST, ACHIEVEMENT;
    }
}
