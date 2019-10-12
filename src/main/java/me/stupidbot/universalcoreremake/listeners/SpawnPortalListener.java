package me.stupidbot.universalcoreremake.listeners;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class SpawnPortalListener implements Listener {
 @EventHandler
    public void OnPlayerInteract(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) { // TODO this is temporary, please make this good and add an effect
            p.teleport(new Location(Bukkit.getWorld("world"), -92.5d, 48d, -69d, -120f, -10f));

            UniversalCoreRemake.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(UniversalCoreRemake.getInstance(), () ->
                    p.setVelocity(new Vector(0, 0, 0))); // Stop fall damage
            e.setCancelled(true);
        }
    }
}