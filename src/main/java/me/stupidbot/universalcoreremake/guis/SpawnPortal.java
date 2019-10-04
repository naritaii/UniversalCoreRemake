package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SpawnPortal implements InventoryProvider {
    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("portal-" + p.getUniqueId().toString())
                .provider(new SpawnPortal())
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(3, 9)
                .title("Fast-Travel").build();
    }

    Map<String, Location> warps  = new HashMap<String, Location>() {{ // TODO this is temporary, please make this good and add an effect
        put("Aoba", new Location(Bukkit.getWorld("world"), -92.5d, 48d, -69d, -120f, -10f));
    }};

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));



        Pagination pagination = contents.pagination();
        ClickableItem[] items = new ClickableItem[warps.size()];
        int i = 0;
        for (Map.Entry<String, Location> e : warps.entrySet()) {
            String id = e.getKey().trim();
            Location loc = e.getValue();
            boolean canWarp = p.hasPermission("universalcore.warp." + id.toLowerCase());
            ItemBuilder di = new ItemBuilder(canWarp ? Material.DIAMOND : Material.COAL).name("&a" + id);

            items[i] = ClickableItem.of(di.build(), ev -> {
                if (canWarp)
                    p.teleport(loc);
            });
            i++;
        }

        pagination.setItems(items);
        int perPage = 7;
        pagination.setItemsPerPage(perPage);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
        int pages = (pagination.getPageItems().length + perPage - 1) / perPage; // Rounds up num / divisor


        if (pagination.getPage() > 1)
             contents.set(1, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).name("&ePrevious Page").build(),
                e -> getInventory(p).open(p, pagination.previous().getPage())));
        if (pagination.getPage() < pages)
             contents.set(1, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).name("&eNext Page").build(),
                e -> getInventory(p).open(p, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}