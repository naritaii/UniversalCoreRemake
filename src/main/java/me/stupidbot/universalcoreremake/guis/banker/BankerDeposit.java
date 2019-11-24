package me.stupidbot.universalcoreremake.guis.banker;

import com.google.common.collect.Lists;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class BankerDeposit implements InventoryProvider {

    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("deposit-" + p.getUniqueId().toString())
                .provider(new BankerDeposit())
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(3, 9)
                .title("Deposit").build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        String bankedS = TextUtils.addCommas(up.getBankedMoney());
        Economy econ = UniversalCoreRemake.getEconomy();
        double balance = econ.getBalance(p);

        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));

        contents.set(1, 1, ClickableItem.of(new ItemBuilder(Material.CHEST).amount(64).name("&aDeposit 100%")
                .lore("&7Current bank balance: &6$" + bankedS)
                .lore("&7Amount to deposit: &6$" + TextUtils.addCommas(balance))
                .lore("")
                .lore("&eClick to deposit!").build(), e -> {
            double deposit = econ.getBalance(p);
            up.addBankedMoney(deposit);
            econ.withdrawPlayer(p, deposit);
            getInventory(p).open(p);
        }));

        contents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.CHEST).amount(32).name("&aDeposit 50%")
                .lore("&7Current bank balance: &6$" + bankedS)
                .lore("&7Amount to deposit: &6$" + TextUtils.addCommas(balance * 0.5d))
                .lore("")
                .lore("&eClick to deposit!").build(), e -> {
            double deposit = econ.getBalance(p) * 0.5d;
            up.addBankedMoney(deposit);
            econ.withdrawPlayer(p, deposit);
            getInventory(p).open(p);
        }));

        contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.CHEST).name("&aDeposit 20%")
                .lore("&7Current bank balance: &6$" + bankedS)
                .lore("&7Amount to deposit: &6$" + TextUtils.addCommas(balance * 0.2d))
                .lore("")
                .lore("&eClick to deposit!").build(), e -> {
            double deposit = econ.getBalance(p) * 0.2d;
            up.addBankedMoney(deposit);
            econ.withdrawPlayer(p, deposit);
            getInventory(p).open(p);
        }));

        contents.set(1, 7, ClickableItem.of(new ItemBuilder(Material.CHEST).name("&aDeposit Specific Amount")
                        .lore("&7Current bank balance: &6$" + bankedS)
                        .lore("")
                        .lore("&eClick to deposit a specific amount!").build(), e -> UniversalCoreRemake.getSignGui().newMenu(p, Lists.newArrayList("",
                                "&e^^^^^^^^^^^^^^^", "&rEnter the amount", "&rto deposit"))
                                .response((player, lines) -> {
                                    try {
                                        double deposit = Math.min(Math.abs(Double.parseDouble(lines[0])), UniversalCoreRemake.getEconomy().getBalance(p));
                                        up.addBankedMoney(deposit);
                                        UniversalCoreRemake.getEconomy().withdrawPlayer(p, deposit);
                                        getInventory(p).open(p);
                                        return true;
                                    } catch (NumberFormatException nfe) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid number."));
                                        getInventory(p).open(p);
                                        return false;
                                    }
                                }).open()
        ));

        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.ARROW).name("&eGo Back").build(), e ->
                BankerHomePage.getInventory(p).open(p)));
    }

    @Override
    public void update(Player player, InventoryContents contents) { /* Do nothing */ }
}