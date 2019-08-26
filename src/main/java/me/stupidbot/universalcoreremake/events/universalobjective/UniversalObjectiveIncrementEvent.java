package me.stupidbot.universalcoreremake.events.universalobjective;

import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UniversalObjectiveIncrementEvent extends Event {
    private final Player player;
    private final UniversalObjective universalObjective;
    private final int newProgress;
    private final int oldProgress;
    private final int needed;

    public UniversalObjectiveIncrementEvent(Player player, UniversalObjective universalObjective, int newProgress,
                                            int oldProgress, int needed) {
        this.player = player;
        this.universalObjective = universalObjective;
        this.newProgress = newProgress;
        this.oldProgress = oldProgress;
        this.needed = needed;
    }

    public Player getPlayer() {
        return player;
    }

    public UniversalObjective getUniversalObjective() {
        return universalObjective;
    }

    public int getNewProgress() {
        return newProgress;
    }

    public int getOldProgress() {
        return oldProgress;
    }

    public int getNeeded() {
        return needed;
    }

    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
