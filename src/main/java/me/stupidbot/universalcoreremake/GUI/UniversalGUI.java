package me.stupidbot.universalcoreremake.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class UniversalGUI {
    public static Map<UUID, UniversalGUI> inventoriesByUuid = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();

    private UUID uuid;
    private Inventory inventory;
    private Map<Integer, UniversalGUIAction> actions;

    public interface UniversalGUIAction {
        void click(Player player);
    }

    public UniversalGUI(int inventorySize, String inventoryName) {
        inventory = Bukkit.createInventory(null, inventorySize, inventoryName);
        actions = new HashMap<>();
        inventoriesByUuid.put(uuid = UUID.randomUUID(), this);
    }

    public final void setItem(int slot, ItemStack stack, UniversalGUIAction action) {
        inventory.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    public final void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, null);
    }

    public final void open(Player player) {
        player.openInventory(inventory);

        openInventories.put(player.getUniqueId(), getUUID());
    }

    public final void delete() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (getOpenInventories().get(player.getUniqueId()).equals(getUUID())) {
                player.closeInventory();
            }
        }

        getInventoriesByUUID().remove(getUUID());
    }

    public final Inventory getInventory() {
        return inventory;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Map<Integer, UniversalGUIAction> getActions() {
        return actions;
    }

    public static Map<UUID, UniversalGUI> getInventoriesByUUID() {
        return inventoriesByUuid;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }
}
