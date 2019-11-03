package me.stupidbot.universalcoreremake.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LevelUpEvent extends Event {
    private final Player player;
    private final int level;
    private final int xp;

    public LevelUpEvent(Player player, int level, int xp) {
        this.player = player;
        this.level = level;
        this.xp = xp;
    }

    public Player getPlayer() {
        return player;
    }

    public int getLevel() {
        return level;
    }

    @SuppressWarnings("unused")
    public int getXp() {
        return xp;
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
