package me.stupidbot.universalcoreremake.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftingListener implements Listener {
    @EventHandler
    public void OnPlayerCraft(CraftItemEvent e) {
        if (!e.getWhoClicked().hasPermission("universalcore.admin"))
            e.setCancelled(true);
    }
}
