package me.stupidbot.universalcoreremake.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpawnPortalListener implements Listener {
    @EventHandler
    public void OnPlayerInteract(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) { // TODO this is temporary, please make this good and add an effect
            p.teleport(new Location(Bukkit.getWorld("world"), -92.5d, 48d, -69d, -120f, -10f));
            e.setCancelled(true);
        }
    }
}