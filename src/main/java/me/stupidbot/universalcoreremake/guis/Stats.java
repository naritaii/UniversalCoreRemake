package me.stupidbot.universalcoreremake.guis;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.Skull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Stats implements InventoryProvider {
    private final Player p;

    private Stats(Player p) {
        this.p = p;
    }

    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("stats-" + p.getUniqueId().toString())
                .provider(new Stats(p))
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(3, 9)
                .title(p.getName() + "'s Prisons Statistics").build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));

        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        contents.set(1, 4, ClickableItem.empty(new ItemBuilder(Skull.getPlayerSkull(p.getName()))
                .name("&aOverall Statistics")
                .lore("&7You were the &a" + TextUtils.getDayNumberSuffix(up.getJoinNumber()) +
                        "&7 player to log into Corrupt Prisons!")
                .lore("&7First Login: &a" + up.getFirstPlayed())
                .lore("&7Time Played: &a" + TextUtils.secondsToString(up.getSecondsPlayed()))
                .lore("&7Total XP Gained: &a" + TextUtils.addCommas(up.getTotalXp()))
                .lore("&7Total Money Gained: &a$" + TextUtils.addCommas(up.getTotalMoney()))
                .lore("&7Blocks Mined: &a" + TextUtils.addCommas(up.getBlocksMined()))
                .lore("&7Items Levelled: &a" + TextUtils.addCommas(up.getItemsLevelled()))
                .lore("&7Kills: &a" + TextUtils.addCommas(up.getDeaths()))
                .lore("&7Deaths: &a" + TextUtils.addCommas(up.getKills()))
                .lore("&7Kill/Death Ratio: &a" + (up.getDeaths() > 0 ? TextUtils.addCommas(
                        (double) up.getKills() / (double) up.getDeaths()) :
                        TextUtils.addCommas((double) up.getKills())))
                .build()));
    }

    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}