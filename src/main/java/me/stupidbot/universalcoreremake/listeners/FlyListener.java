package me.stupidbot.universalcoreremake.listeners;

import com.sk89q.worldguard.protection.flags.StateFlag;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.worldguard.RegionEnterEvent;
import me.stupidbot.universalcoreremake.events.worldguard.RegionLeftEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FlyListener implements Listener {
    @EventHandler
    public void OnRegionEnter(RegionEnterEvent e) {
        Player p = e.getPlayer();
        if (UniversalCoreRemake.UNIVERSAL_FLY != null &&
                e.getRegion().getFlag(UniversalCoreRemake.UNIVERSAL_FLY) == StateFlag.State.ALLOW &&
                p.hasPermission("universalcore.fly"))
            p.performCommand("/fly");
    }

    @EventHandler
    public void OnRegionLeave(RegionLeftEvent e) {
        Player p = e.getPlayer();
        if (UniversalCoreRemake.UNIVERSAL_FLY != null &&
                e.getRegion().getFlag(UniversalCoreRemake.UNIVERSAL_FLY) == StateFlag.State.DENY &&
                p.getAllowFlight() &&
                !p.hasPermission("universalcore.admin")) {
            p.setAllowFlight(false);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFlight disabled."));
        }
    }
}
