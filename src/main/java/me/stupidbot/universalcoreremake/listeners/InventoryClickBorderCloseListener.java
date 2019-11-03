package me.stupidbot.universalcoreremake.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickBorderCloseListener implements Listener {
    @EventHandler
        public void OnInventoryClick(InventoryClickEvent e) {
            if (e.getClick() == ClickType.WINDOW_BORDER_LEFT)
                e.getWhoClicked().closeInventory();
        }
}
