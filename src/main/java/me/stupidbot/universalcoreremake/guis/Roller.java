package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.RandomCollection;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Roller implements InventoryProvider {
    private final String title;
    private final RandomCollection<StringReward> loot;

    private Roller(String title, RandomCollection<StringReward> loot) {
        this.title = title;
        this.loot = loot;
    }

    public static SmartInventory getInventory(Player p, String title, RandomCollection<StringReward> loot) {
        return SmartInventory.builder()
                .id(title + "-" + p.getUniqueId().toString())
                .provider(new Roller(title, loot))
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(6, 9)
                .title(title).build();
    }

    private final int[][] spinSlots = new int[][]{{0, 5}, {1, 4}, {1, 3}, {2, 2}, {3, 2}, {4, 1},
            {5, 2}, {6, 2}, {7, 3}, {7, 4}, {8, 5}};
    private final int winIndex = 5;

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));

        for (int[] i : spinSlots)
            contents.set(i[0], i[1], ClickableItem.empty(new ItemStack(Material.AIR)));
    }


    @Override
    public void update(Player player, InventoryContents contents) {

    }
}