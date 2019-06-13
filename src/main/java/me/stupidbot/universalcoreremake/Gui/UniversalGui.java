package me.stupidbot.universalcoreremake.Gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UniversalGui {
    private static Map<UUID, UniversalGui> inventoriesByUuid;
    private static Map<UUID, UUID> openInventories;

    static {
        inventoriesByUuid = new HashMap<>();
        openInventories = new HashMap<>();
    }

    private UUID uuid;
    private Inventory inventory;
    private Map<Integer, UniversalGuiAction> actions;

    public interface UniversalGuiAction {
        void click(Player player);
    }

    public UniversalGui(int inventorySize, String inventoryName) {
        inventory = Bukkit.createInventory(null, inventorySize, inventoryName);
        actions = new HashMap<>();
        inventoriesByUuid.put(uuid = UUID.randomUUID(), this);
    }

    public final void setItem(int slot, ItemStack stack, UniversalGuiAction action) {
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

        openInventories.put(player.getUniqueId(), getUuid());
    }

    public final void delete() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (getOpenInventories().get(player.getUniqueId()).equals(getUuid())) {
                player.closeInventory();
            }
        }

        getInventoriesByUuid().remove(getUuid());
    }

    public final Inventory getInventory() {
        return inventory;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Map<Integer, UniversalGuiAction> getActions() {
        return actions;
    }

    public static final Map<UUID, UniversalGui> getInventoriesByUuid() {
        return inventoriesByUuid;
    }

    public static final Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }
}
