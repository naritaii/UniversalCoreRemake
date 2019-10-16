package me.stupidbot.universalcoreremake.listeners;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.Warp;
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
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            Warp warp = Warp.getWarpFromId(UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getSelectedWarpId());
            warp.warp(p);

            UniversalCoreRemake.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(UniversalCoreRemake.getInstance(), () ->
                    p.setVelocity(new Vector(p.getVelocity().getX(), 0.2, p.getVelocity().getZ()))); // Stop fall damage and looks cool
            e.setCancelled(true);
        }
    }
}