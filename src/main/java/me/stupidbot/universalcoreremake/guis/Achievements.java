package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.Skull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Achievements implements InventoryProvider {
    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("achievements-" + p.getUniqueId().toString())
                .provider(new Achievements())
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(3, 9)
                .title("Achievements").build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));

        contents.set(0, 0, ClickableItem.of(new ItemBuilder(Skull.getPlayerSkull(p.getName())).name("&aStats").build(),
                e -> Stats.getInventory(p).open(p)));


        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        List<UniversalObjective> achievements = UniversalCoreRemake.getUniversalObjectiveManager().achievements;
        List<String> completed = up.getCompletedObjectives();

        Pagination pagination = contents.pagination();
        ClickableItem[] achievementItems = new ClickableItem[achievements.size()];
        for (int i = 0; i < achievements.size(); i++) {
            UniversalObjective o = achievements.get(i);

            int needed = UniversalCoreRemake.getUniversalObjectiveManager().getNeeded(o);
            if (completed.contains(o.getId())) {
                ItemBuilder di = new ItemBuilder(Material.DIAMOND);
                di.name(o.getDisplayItem().getItemMeta().getDisplayName() + " &c&lCOMPLETED");

                if (needed > 1)
                    if (completed.contains(o.getId()))
                        di.lore("&8(&b" + needed + "&7/&b" + needed + "&8)");
                    else
                        di.lore("&8(&a" + o.getProgress(p) + "&7/&b" + needed + "&8)");

                achievementItems[i] = ClickableItem.empty(di.build());
            } else {
                ItemBuilder di = new ItemBuilder(Material.COAL);
                di.name(o.getDisplayItem().getItemMeta().getDisplayName());

                if (needed > 1)
                    if (completed.contains(o.getId()))
                        di.lore("&8(&b" + needed + "&7/&b" + needed + "&8)");
                    else
                        di.lore("&8(&a" + o.getProgress(p) + "&7/&b" + needed + "&8)");

                achievementItems[i] = ClickableItem.empty(di.build());
            }
        }

        pagination.setItems(achievementItems);
        int perPage = 7;
        pagination.setItemsPerPage(perPage);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
        // int pages = (pagination.getPageItems().length + (perPage - 1)) / perPage; // Rounds up num / divisor
        int pages = Math.floorDiv(achievementItems.length -1, perPage); // Rounds down

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