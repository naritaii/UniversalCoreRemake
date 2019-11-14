package me.stupidbot.universalcoreremake.guis.banker;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BankerHomePage implements InventoryProvider {

    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("banker-" + p.getUniqueId().toString())
                .provider(new BankerHomePage())
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(3, 9)
                .title("Banker").build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        String banked = TextUtils.addCommas(up.getBankedMoney());

        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));

        contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.CHEST).name("&aDeposit Money")
                .lore("&7Current bank balance: &6$" + banked)
                .lore("")
                .lore("&7Store money with us to keep")
                .lore("&7it safe while you adventure!")
                .lore("")
                .lore("&eClick to make a deposit!").build(), e -> BankerDeposit.getInventory(p).open(p)));

        contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.DISPENSER).name("&aWithdraw Money")
                .lore("&7Current bank balance: &6$" + banked)
                .lore("")
                .lore("&7Take your money back from our")
                .lore("&7thieving mitts to spend them!")
                .lore("")
                .lore("&eClick to withdraw money!").build(), e -> BankerWithdraw.getInventory(p).open(p)));
    }

    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}