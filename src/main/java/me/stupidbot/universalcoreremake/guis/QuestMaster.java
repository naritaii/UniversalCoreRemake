package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class QuestMaster implements InventoryProvider {
    private final Player p;

    private QuestMaster(Player p) {
        this.p = p;
    }

    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("questmaster-" + p.getUniqueId().toString())
                .provider(new QuestMaster(p))
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(5, 9)
                .title("Quest Master").build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));

        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
    }

    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}