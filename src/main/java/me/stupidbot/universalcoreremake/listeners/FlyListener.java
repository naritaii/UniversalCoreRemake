package me.stupidbot.universalcoreremake.listeners;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.worldguard.RegionEnterEvent;
import me.stupidbot.universalcoreremake.events.worldguard.RegionLeftEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FlyListener implements Listener {
    @EventHandler
    public void OnRegionEnter(RegionEnterEvent e) {
        Player p = e.getPlayer();
        if (UniversalCoreRemake.UNIVERSAL_FLY != null &&
                e.getRegion().getFlag(UniversalCoreRemake.UNIVERSAL_FLY) == StateFlag.State.ALLOW &&
                !p.getAllowFlight() &&
                p.hasPermission("universalcore.fly")) {
            p.setAllowFlight(true);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFlight enabled."));
        }
    }

    @EventHandler
    public void OnRegionLeave(RegionLeftEvent e) {
        Player p = e.getPlayer();
        disableFlight(p);
    }

    @EventHandler
    public void OnPlayerTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        disableFlight(p, e.getTo());
    }

    private void disableFlight(Player p) {
        disableFlight(p, p.getLocation());
    }

    private void disableFlight(Player p, Location loc) {
        if (UniversalCoreRemake.UNIVERSAL_FLY != null &&
                p.getAllowFlight() &&
                !p.hasPermission("universalcore.admin")) {
            boolean canFly = false;
            ApplicableRegionSet regions = UniversalCoreRemake.getWorldGuardPlugin().getRegionManager(loc.getWorld())
                    .getApplicableRegions(loc);
            for (ProtectedRegion region : regions) {
                StateFlag.State f = region.getFlag(UniversalCoreRemake.UNIVERSAL_FLY);
                if (f == StateFlag.State.ALLOW) {
                    canFly = true;
                    break;
                }
            }

            if (!canFly) {
                p.setAllowFlight(false);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFlight disabled."));
            }
        }
    }
}
