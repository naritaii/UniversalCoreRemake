package me.stupidbot.universalcoreremake.listeners;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.Warp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpawnPortalListener implements Listener {
    private final List<UUID> ids = new ArrayList<>();

    @EventHandler
    public void OnPlayerInteract(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL && !ids.contains(p.getUniqueId())) {
            ids.add(p.getUniqueId());
            Warp warp = Warp.getWarpFromId(UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getSelectedWarpId());
            warp.warp(p);

            UniversalCoreRemake.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(UniversalCoreRemake.getInstance(), () -> {
                p.setVelocity(new Vector(p.getVelocity().getX(), 0, p.getVelocity().getZ()));
                ids.remove(p.getUniqueId());
            }); // Stop fall damage/teleporting many times
            e.setCancelled(true);
        }
    }
}