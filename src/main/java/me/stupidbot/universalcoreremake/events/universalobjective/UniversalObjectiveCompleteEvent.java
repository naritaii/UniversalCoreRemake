package me.stupidbot.universalcoreremake.events.universalobjective;

import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UniversalObjectiveCompleteEvent extends Event {
    private final Player player;
    private final UniversalObjective universalObjective;

    public UniversalObjectiveCompleteEvent(Player player, UniversalObjective universalObjective) {
        this.player = player;
        this.universalObjective = universalObjective;
    }

    public Player getPlayer() {
        return player;
    }

    public UniversalObjective getUniversalObjective() {
        return universalObjective;
    }

    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
