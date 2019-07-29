package me.stupidbot.universalcoreremake.Events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UniversalBlockBreakEvent extends Event {
    private final Player player;
    private final Block block;
    private int amount;
    private int xp;
    private int stamina;

    public UniversalBlockBreakEvent(Player player, Block block, int amount, int xp, int stamina) {
        this.player = player;
        this.block = block;
        setAmount(amount);
        setXp(xp);
        setStamina(stamina);
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public int getAmount() {
        return amount;
    }

    private void setAmount(int amount) {
        this.amount = amount;
    }

    public int getXp() {
        return xp;
    }

    private void setXp(int xp) {
        this.xp = xp;
    }

    public int getStamina() {
        return stamina;
    }

    private void setStamina(int stamina) {
        this.stamina = stamina;
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
