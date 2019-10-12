package me.stupidbot.universalcoreremake.listeners;

import me.stupidbot.universalcoreremake.guis.QuestMaster;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderchestListener implements Listener {
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
            if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                p.playSound(e.getClickedBlock().getLocation(), Sound.CHEST_OPEN, 1, 1);
                QuestMaster.getInventory(p).open(p);
                e.setCancelled(true);
            }
    }
}