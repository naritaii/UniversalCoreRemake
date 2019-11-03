package me.stupidbot.universalcoreremake.guis;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.managers.RewardManager;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.StringReward;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.Warp;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.Skull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.util.*;

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
        // Misc
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build())); // TODO Daily rewards

        contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER).name("&cClose").build(),
                e -> p.closeInventory()));

        contents.set(5, 2, ClickableItem.of(new ItemBuilder(Material.ENDER_CHEST).name("&aEnder Chest")
                .lore("").lore("&eClick to store items!").build(), e -> p.openInventory(p.getEnderChest())));

        boolean isInSpawn = false;
        ApplicableRegionSet regions = UniversalCoreRemake.getWorldGuardPlugin().getRegionManager(p.getWorld())
                .getApplicableRegions(p.getLocation());
        for (ProtectedRegion region : regions)
            if (region.getId().equalsIgnoreCase("spawn")) {
                isInSpawn = true;
                break;
            }
        Warp warp = isInSpawn ? Warp.getWarpFromId(UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p).getSelectedWarpId()) :
                new Warp(new Location(Bukkit.getWorld("world"), 15214.5, 65, 10152.5, -180, 0), "hub", "Hub");
        contents.set(5, 3, ClickableItem.of(new ItemBuilder(Skull.getCustomSkull(Skull.NETHER_PORTAL.getId()))
                .name("&bWarp to: &e" + warp.getName()).lore("").lore("&eClick to warp!").build(), e -> warp.warp(p)));

        contents.set(5, 5, ClickableItem.of(new ItemBuilder(Skull.getCustomSkull(Skull.DISCORD.getId()))
                .name("&5Discord").lore("&eJoin our Discord for updates, giveaways, and to\n" +
                        "&edirectly give feedback to admins and the community!\n\n" +
                        "&bType &d/sync Discord&b to sync your Minecraft and Discord accounts.").build(), e ->
                p.performCommand("discord")));

        contents.set(5, 6, ClickableItem.of(new ItemBuilder(Skull.getCustomSkull(Skull.TWITTER.getId()))
                .name("&bTwitter").lore("&eFollow our Twitter for updates and giveaways.").build(), e ->
                p.performCommand("twitter")));

        // Rewards
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        RewardManager rm = UniversalCoreRemake.getRewardManager();

        int streak = up.getStreak("Vote");
        int maxStreak = rm.votingRewards.size();
        while (streak > maxStreak)
            streak -= maxStreak;
        boolean canVote = false;
        try {
            canVote = up.getRewardTimestamp("Vote") == null || rm.checkDaysBetween(up.getRewardTimestamp("Vote"),
                    RewardManager.getSimpleDateFormat().format(new Date())) > 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ItemBuilder vote = new ItemBuilder(new ItemStack(canVote ? Material.STORAGE_MINECART : Material.HOPPER_MINECART));
        vote.name((canVote ? "&a" : "&c") + "Vote Delivery");

        if (canVote) {
            StringBuilder streakLore = new StringBuilder("&7Streak: ");
            for (int i = 1; i <= maxStreak; i++)
                streakLore.append(i == streak + 1 ? ChatColor.GOLD : (i < streak + 1 ? ChatColor.YELLOW :
                        ChatColor.DARK_GRAY))
                        .append(TextUtils.toBallNumber(i));
            StringBuilder rewardLore = new StringBuilder("&aStreak Reward: ");
            StringReward rewards = rm.votingRewards.get(streak);
            if (rewards != null) {
                String[] asStrings = rewards.asStrings();
                if (asStrings != null) {
                    for (String s : asStrings)
                        if (s != null)
                            rewardLore.append("\n  &8+").append(s);
                } else
                    rewardLore.append("&8NONE");
            } else
                rewardLore.append("&8NONE");


            vote.lore("&7Vote for for us DAILY to")
                    .lore("&7get better and BETTER free loot!")
                    .lore("")
                    .lore(streakLore.toString());

            for (String l : rewardLore.toString().split("\n"))
                vote.lore(l);

            vote.lore("")
                    .lore("&eClick to vote Corrupt Prisons as the top server!");

            vote.enchantment(UniversalEnchantment.GLOW);
        } else
            vote.lore("&7Vote for us again tomorrow")
                    .lore("&7for better rewards!") // TODO add countdown to next vote/reload gui if player votes
                    .lore("")
                    .lore("&eClick to vote for us again!");

        contents.set(3, 4, ClickableItem.of(vote.build(), e -> p.performCommand("vote")));

        // Quests
        Map<String, Integer> dic = UniversalCoreRemake.getUniversalObjectiveManager().registeredObjectivesDictionary;
        List<UniversalObjective> uo = new ArrayList<>();
        List<String> ids = up.getSelectedObjectives();
        Collections.reverse(ids);
        List<String> completed = up.getCompletedObjectives();
        Collections.reverse(completed);
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
        ClickableItem[] quests = new ClickableItem[uo.size() + 1];
        for (int i = 0; i < uo.size(); i++) {
            UniversalObjective o = uo.get(i);
            int slot = i;

            if (i >= ids.size() - completed.size())
                if (i == ids.size() - completed.size()) {
                    quests[slot++] = ClickableItem.empty(new ItemBuilder(new ItemStack(
                            Material.STAINED_GLASS_PANE, 1, (short) 5)).name("&2<- &aIn progress")
                            .lore("&aCompleted &2->").build());
                } else
                    slot++;


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
                    di.lore("&8(&b" + needed + "&7/&b" + needed + "&8)");
                else
                    di.lore("&8(&a" + o.getProgress(p) + "&7/&b" + needed + "&8)");

            quests[slot] = ClickableItem.empty(di.build());
        }

        pagination.setItems(quests);
        int perPage = 7;
        pagination.setItemsPerPage(perPage);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
        // int pages = (pagination.getPageItems().length + (perPage - 1)) / perPage; // Rounds up num / divisor
        int pages = Math.floorDiv(quests.length - 1, perPage); // Rounds down


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