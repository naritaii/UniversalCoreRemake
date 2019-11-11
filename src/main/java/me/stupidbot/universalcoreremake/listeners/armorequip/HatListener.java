package me.stupidbot.universalcoreremake.listeners.armorequip;

import me.stupidbot.universalcoreremake.events.armorequip.ArmorEquipEvent;
import me.stupidbot.universalcoreremake.events.armorequip.ArmorType;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Bukkit;
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

                if (ItemMetadata.hasMeta(i, "HAT"))
                    if (p.getInventory().getHelmet() == null ||
                            p.getInventory().getHelmet().getType() != Material.AIR) {
                        e.setCancelled(true);
                        p.getInventory().setHelmet(i);
                        p.setItemInHand(new ItemStack(Material.AIR));
                        ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.HELMET, null, i);
                        p.updateInventory();
                        Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                    }
            }
        }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER &&
                e.getClick() == ClickType.LEFT && e.getSlot() == 39 &&
                e.getCursor().getType() != Material.AIR) {
            if (ItemMetadata.hasMeta(e.getCursor(), "HAT") ||
                    e.getWhoClicked().hasPermission("universalcore.hat")) {
                e.setCancelled(true);
                ItemStack helm = e.getWhoClicked().getInventory().getHelmet();
                ItemStack cursor = e.getCursor();
                e.setCurrentItem(cursor);
                e.getWhoClicked().setItemOnCursor(helm);
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, ArmorType.HELMET, helm, cursor);
                ((Player) e.getWhoClicked()).updateInventory();
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            }
        }

        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER &&
                e.getClick() == ClickType.SHIFT_LEFT &&
                (e.getWhoClicked().getInventory().getHelmet() == null ||
                e.getWhoClicked().getInventory().getHelmet().getType() == Material.AIR) &&
                e.getCurrentItem().getType() != Material.AIR) {
            if (ItemMetadata.hasMeta(e.getCurrentItem(), "HAT") ||
                    e.getWhoClicked().hasPermission("universalcore.hat")) {
                e.setCancelled(true);
                e.getWhoClicked().getInventory().setHelmet(e.getCurrentItem());
                e.setCurrentItem(new ItemStack(Material.AIR));
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, ArmorType.HELMET, null, e.getCurrentItem());
                ((Player) e.getWhoClicked()).updateInventory();
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            }
        }
    }
}
