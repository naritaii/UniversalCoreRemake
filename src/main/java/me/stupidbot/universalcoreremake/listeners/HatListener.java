package me.stupidbot.universalcoreremake.listeners;

import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HatListener implements Listener {
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            ItemStack i = p.getItemInHand();

            if (ItemMetadata.hasMeta(i, "HAT") || (i != null && i.getType() == Material.SKULL_ITEM))
                if (p.getInventory().getHelmet() == null ||
                        p.getInventory().getHelmet().getType() == Material.AIR) {
                    e.setCancelled(true);
                    p.getInventory().setHelmet(i);
                    p.setItemInHand(new ItemStack(Material.AIR));
                }
        }
    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER &&
                e.getClick() == ClickType.LEFT && e.getSlot() == 39 &&
                e.getCursor().getType() != Material.AIR)
            if (ItemMetadata.hasMeta(e.getCursor(), "HAT") ||
                    e.getWhoClicked().hasPermission("universalcore.hat")) {
                e.setCancelled(true);
                ItemStack helm = e.getWhoClicked().getInventory().getHelmet();
                ItemStack cursor = e.getCursor();
                e.setCurrentItem(cursor);
                e.getWhoClicked().setItemOnCursor(helm);
            }

        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER &&
                e.getClick() == ClickType.SHIFT_LEFT &&
                (e.getWhoClicked().getInventory().getHelmet() == null ||
                        e.getWhoClicked().getInventory().getHelmet().getType() == Material.AIR) &&
                e.getCurrentItem().getType() != Material.AIR)
            if (e.getCurrentItem().getType() == Material.SKULL_ITEM ||
                    ItemMetadata.hasMeta(e.getCurrentItem(), "HAT")) {
                e.setCancelled(true);
                e.getWhoClicked().getInventory().setHelmet(e.getCurrentItem());
                e.setCurrentItem(new ItemStack(Material.AIR));
            }
    }
}
