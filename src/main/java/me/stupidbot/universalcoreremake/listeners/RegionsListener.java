package me.stupidbot.universalcoreremake.listeners;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.worldguard.RegionEnterEvent;
import me.stupidbot.universalcoreremake.events.worldguard.RegionLeftEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RegionsListener implements Listener {
    private final RegionContainer container = UniversalCoreRemake.getWorldGuardPlugin().getRegionContainer();

    private final HashMap<UUID, Set<ProtectedRegion>> playerRegions = new HashMap<>();

/*    public HashMap<UUID, Set<ProtectedRegion>> getPlayerRegions() {
        return playerRegions;
    }*/

/*    public Set<ProtectedRegion> getPlayerRegions(UUID uuid) {
        playerRegions.putIfAbsent(uuid, new HashSet<>());
        return playerRegions.get(uuid);
    }*/

    private Set<ProtectedRegion> getRegions(UUID u) {
        Player p = Bukkit.getPlayer(u);

        // If player is offline
        if (p == null)
            return new HashSet<>();

        Location l = p.getLocation();

        RegionQuery q = container.createQuery();
        ApplicableRegionSet ars = q.getApplicableRegions(l);
        return ars.getRegions();
    }

    private void changeRegions(UUID u, Set<ProtectedRegion> actual) {
        playerRegions.putIfAbsent(u, new HashSet<>());
        // If the sets contain the same info, ignore.
        int previousSize = playerRegions.get(u).size();
        int actualSize = actual.size();

        if (actual.size() == playerRegions.get(u).size() && actual.containsAll(playerRegions.get(u)))
            return;

        Set<ProtectedRegion> previous = playerRegions.get(u);

        boolean joined = false;
        boolean left = false;

        // Check if we joined and/or left a region.
        if (actualSize == previousSize) {
            joined = true;
            left = true;
        } else if (actualSize < previousSize)
            left = true;
        else
            joined = true;

        if (left) {
            // If we left a region
            Set<ProtectedRegion> leftRegions = new HashSet<>(previous);
            leftRegions.removeAll(actual);
            for (ProtectedRegion region : leftRegions) {
                RegionLeftEvent re = new RegionLeftEvent(Bukkit.getPlayer(u), region);
                Bukkit.getPluginManager().callEvent(re);
            }
        }

        if (joined) {
            //If we entered a region
            Set<ProtectedRegion> enteredRegions = new HashSet<>(actual);
            enteredRegions.removeAll(previous);
            for (ProtectedRegion region : enteredRegions) {
                RegionEnterEvent re = new RegionEnterEvent(Bukkit.getPlayer(u), region);
                Bukkit.getPluginManager().callEvent(re);
            }
        }

        // Apply the changes
        playerRegions.put(u, actual);
    }

    private void quit(UUID u) {
        Set<ProtectedRegion> empty = new HashSet<>();
        changeRegions(u, empty);
    }

    private void changeWorld(UUID u) {
        changeRegions(u, getRegions(u));
    }
    
    @EventHandler // TODO Find a way that won't be called every tick and works on teleport
    public void OnPlayerMove(PlayerMoveEvent e) {
        changeRegions(e.getPlayer().getUniqueId(), getRegions(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void OnJoin(PlayerJoinEvent e) {
        changeRegions(e.getPlayer().getUniqueId(), getRegions(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void OnQuit(PlayerQuitEvent e) {
        quit(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void OnKick(PlayerKickEvent e) {
        quit(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void OnRespawn(PlayerRespawnEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        changeRegions(uuid, getRegions(uuid));
    }

    @EventHandler
    public void OnDeath(PlayerDeathEvent e) {
        quit(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void OnWorldChange(PlayerChangedWorldEvent e) {
        changeWorld(e.getPlayer().getUniqueId());
    }
}