package me.stupidbot.universalcoreremake.Listeners;

import me.stupidbot.universalcoreremake.GUI.UniversalGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UniversalGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) e.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        UUID inventoryUUID = UniversalGUI.getOpenInventories().get(playerUUID);
        if (inventoryUUID != null) {
            e.setCancelled(true);
            UniversalGUI gui = UniversalGUI.getInventoriesByUUID().get(inventoryUUID);
            UniversalGUI.UniversalGUIAction action = gui.getActions().get(e.getSlot());

            if (action != null)
                action.click(player);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        UniversalGUI.openInventories.remove(playerUUID);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        UniversalGUI.openInventories.remove(playerUUID);
    }
}
