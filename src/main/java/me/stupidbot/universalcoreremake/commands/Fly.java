package me.stupidbot.universalcoreremake.commands;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class Fly {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.fly"))
            if (s instanceof Player) {
                Player p = (Player) s;

                if (p.getAllowFlight()) {
                    p.setAllowFlight(false);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFlight disabled."));
                } else {
                    boolean canFly = false;
                    ApplicableRegionSet regions = UniversalCoreRemake.getWorldGuardPlugin().getRegionManager(p.getWorld())
                            .getApplicableRegions(p.getLocation());
                    for (ProtectedRegion region : regions) {
                        if (UniversalCoreRemake.UNIVERSAL_FLY == null)
                            break;

                        StateFlag.State f = region.getFlag(UniversalCoreRemake.UNIVERSAL_FLY);
                        if (f == StateFlag.State.ALLOW || p.hasPermission("universalcore.admin")) {
                            canFly = true;
                            break;
                        }
                    }

                    if (canFly) {
                        p.setAllowFlight(true);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFlight enabled."));
                    } else
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot fly right now."));
                }
            } else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cOnly players may use this command!"));
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}