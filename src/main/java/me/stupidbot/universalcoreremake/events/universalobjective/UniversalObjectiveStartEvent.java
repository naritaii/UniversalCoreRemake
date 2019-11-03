package me.stupidbot.universalcoreremake.events.universalobjective;

import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UniversalObjectiveStartEvent extends Event {
    private final Player player;
    private final UniversalObjective universalObjective;
    private final int needed;

    public UniversalObjectiveStartEvent(Player player, UniversalObjective universalObjective, int needed) {
        this.player = player;
        this.universalObjective = universalObjective;
        this.needed = needed;
    }

    public Player getPlayer() {
        return player;
    }

    public UniversalObjective getUniversalObjective() {
        return universalObjective;
    }

    @SuppressWarnings("unused")
    public int getNeeded() {
        return needed;
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
