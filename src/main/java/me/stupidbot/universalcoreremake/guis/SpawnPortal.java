package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.Warp;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SpawnPortal implements InventoryProvider {
    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("portal-" + p.getUniqueId().toString())
                .provider(new SpawnPortal())
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(3, 9)
                .title("Fast-Travel").build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));



        Pagination pagination = contents.pagination();
        List<Warp> warps = Warp.getWarps();
        ClickableItem[] items = new ClickableItem[warps.size()];
        int i = 0;
        for (Warp w : warps) {
            String name = w.getName();
            String id = w.getId();
            boolean canWarp = p.hasPermission("universalcore.warp." + id);
            ItemBuilder di = new ItemBuilder(canWarp ? Material.EYE_OF_ENDER : Material.FIREWORK_CHARGE)
                    .name("&bSet portal to &a" + name)
                    .lore("")
                    .lore(canWarp ? "&eClick to change the portal warp location then jump in!" :
                            "&cYou have not unlocked this location.");

            items[i] = ClickableItem.of(di.build(), e -> {
                if (canWarp) {
                    UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).setSelectedWarpId(id);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aWarp changed! Jump into the portal to teleport to &e" + name));
                } else
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cYou do not have this location unlocked."));
                p.closeInventory();
            });
            i++;
        }

        pagination.setItems(items);
        int perPage = 7;
        pagination.setItemsPerPage(perPage);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
        int pages = Math.floorDiv(warps.size(), perPage);


        if (!pagination.isFirst())
             contents.set(1, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).name("&ePrevious Page").build(),
                e -> getInventory(p).open(p, pagination.previous().getPage())));
        if (pagination.getPage() < pages)
             contents.set(1, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).name("&eNext Page").build(),
                e -> getInventory(p).open(p, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}