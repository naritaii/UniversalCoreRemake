package me.stupidbot.universalcoreremake.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class UniversalGUI {
    private static final Map<UUID, UniversalGUI> inventoriesByUuid = new HashMap<>();
    public static final Map<UUID, UUID> openInventories = new HashMap<>();

    private final UUID uuid;
    private final Inventory inventory;
    private final Map<Integer, UniversalGUIAction> actions;

    public interface UniversalGUIAction {
        void click(Player player);
    }

    UniversalGUI(int inventorySize, String inventoryName) {
        inventory = Bukkit.createInventory(null, inventorySize, inventoryName);
        actions = new HashMap<>();
        inventoriesByUuid.put(uuid = UUID.randomUUID(), this);
    }

    final void setItem(int slot, ItemStack stack, UniversalGUIAction action) {
        inventory.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    final void setItem(int slot, ItemStack stack) {
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

    final Inventory getInventory() {
        return inventory;
    }

    private UUID getUUID() {
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
