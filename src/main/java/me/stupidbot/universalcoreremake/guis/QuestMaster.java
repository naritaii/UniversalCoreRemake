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
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.Skull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestMaster implements InventoryProvider {
    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("questmaster-" + p.getUniqueId().toString())
                .provider(new QuestMaster())
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(6, 9)
                .title("Quest Master").build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));
        
        contents.set(5, 0, ClickableItem.of(new ItemBuilder(Skull.getCustomSkull(Skull.DISCORD.getId()))
                .name("&5Discord").lore("&eJoin our Discord server for updates and giveaways.\n" +
                        "&eThis is a great place to talk to the community and admins directly!\n" +
                        "&eType &d/sync&e to sync your Minecraft and Discord accounts.").build(), e -> {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "\n&5Join our Discord server for news, giveaways, and a way to directly give " +
                                "feedback to our admins and the community!\n&9www.discord.gg/YQsxZh8\n")); // TODO Use Book GUIs
                p.closeInventory();
        }));

        contents.set(5, 1, ClickableItem.of(new ItemBuilder(Skull.getCustomSkull(Skull.TWITTER.getId()))
                .name("&bTwitter").lore("&eFollow our Twitter for updates and giveaways.").build(), e -> {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "\n&bFollow our Twitter for news and giveaways!\n" +
                            "&9www.twitter.com/corruptprisons\n"));
            p.closeInventory();
        }));

        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        Map<String, Integer> dic = UniversalCoreRemake.getUniversalObjectiveManager().registeredObjectivesDictionary;
        List<UniversalObjective> uo = new ArrayList<>();
        List<String> ids = up.getSelectedObjectives();
        List<String> completed = up.getCompletedObjectives();
        ids.addAll(completed);
        for (String sel : ids)
            if (dic.containsKey(sel)) {
                UniversalObjective o = UniversalCoreRemake.getUniversalObjectiveManager().registeredObjectives
                        .get(dic.get(sel));
                if (o.getCategory() == UniversalObjective.Catagory.STORY_QUEST ||
                        o.getCategory() == UniversalObjective.Catagory.CONTEXTUAL_QUEST)
                    uo.add(o);
            }

        Pagination pagination = contents.pagination();
        ClickableItem[] quests = new ClickableItem[uo.size()];
        for (int i = 0; i < uo.size(); i++) {
            UniversalObjective o = uo.get(i);
            ItemBuilder di = new ItemBuilder(o.getDisplayItem());
            int needed = UniversalCoreRemake.getUniversalObjectiveManager().getNeeded(o);
            if (completed.contains(o.getId()))
                di.name("&7" + TextUtils.capitalizeFully(o.getCategory().toString()) + ": " +
                        o.getDisplayItem().getItemMeta().getDisplayName() + " &c&lCOMPLETED");
            else
                di.name("&7" + TextUtils.capitalizeFully(o.getCategory().toString()) + ": " +
                        o.getDisplayItem().getItemMeta().getDisplayName());

            if (needed > 1)
                if (completed.contains(o.getId()))
                    di.lore("&8(&b" + needed + "&7/" + "&b" + needed + "&8)");
                else
                    di.lore("&8(&a" + o.getProgress(p) + "&7/" + "&b" + needed + "&8)");

            quests[i] = ClickableItem.empty(di.build());

        }

        pagination.setItems(quests);
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