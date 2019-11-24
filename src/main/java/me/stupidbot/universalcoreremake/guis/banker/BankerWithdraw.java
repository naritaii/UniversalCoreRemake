package me.stupidbot.universalcoreremake.guis.banker;

import com.google.common.collect.Lists;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.PlayerUtils;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class BankerWithdraw implements InventoryProvider {

    public static SmartInventory getInventory(Player p) {
        return SmartInventory.builder()
                .id("withdraw-" + p.getUniqueId().toString())
                .provider(new BankerWithdraw())
                .manager(UniversalCoreRemake.getInventoryManager())
                .size(3, 9)
                .title("Withdraw").build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        double banked = up.getBankedMoney();
        String bankedS = TextUtils.addCommas(banked);
        Economy econ = UniversalCoreRemake.getEconomy();

        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(
                Material.STAINED_GLASS_PANE, 1, (short) 15)).name(" ").build()));

        contents.set(1, 1, ClickableItem.of(new ItemBuilder(Material.DISPENSER).amount(64).name("&aWithdraw 100%")
                .lore("&7Current bank balance: &6$" + bankedS)
                .lore("&7Amount to withdraw: &6$" + bankedS)
                .lore("")
                .lore("&eClick to withdraw!").build(), e -> {
            double withdraw = up.getBankedMoney();
            up.removeBankedMoney(withdraw);
            PlayerUtils.safeDeposit(p, withdraw);
            getInventory(p).open(p);
        }));

        contents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.DISPENSER).amount(32).name("&aWithdraw 50%")
                .lore("&7Current bank balance: &6$" + bankedS)
                .lore("&7Amount to withdraw: &6$" + TextUtils.addCommas(banked * 0.5))
                .lore("")
                .lore("&eClick to withdraw!").build(), e -> {
            double withdraw = up.getBankedMoney() * 0.5d;
            up.removeBankedMoney(withdraw);
            PlayerUtils.safeDeposit(p, withdraw);
            getInventory(p).open(p);
        }));

        contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.DISPENSER).name("&aWithdraw 20%")
                .lore("&7Current bank balance: &6$" + bankedS)
                .lore("&7Amount to withdraw: &6$" + TextUtils.addCommas(banked * 0.2))
                .lore("")
                .lore("&eClick to withdraw!").build(), e -> {
            double withdraw = up.getBankedMoney() * 0.2d;
            up.removeBankedMoney(withdraw);
            PlayerUtils.safeDeposit(p, withdraw);
            getInventory(p).open(p);
        }));

        contents.set(1, 7, ClickableItem.of(new ItemBuilder(Material.DISPENSER).name("&aWithdraw Specific Amount")
                        .lore("&7Current bank balance: &6$" + bankedS)
                        .lore("")
                        .lore("&eClick to withdraw a specific amount!").build(), e -> UniversalCoreRemake.getSignGui().newMenu(p, Lists.newArrayList("",
                                "&e^^^^^^^^^^^^^^^", "&rEnter the amount", "&rto withdraw"))
                                .response((player, lines) -> {
                                    try {
                                        double withdraw = Math.min(Math.abs(Double.parseDouble(lines[0])), up.getBankedMoney());
                                        up.removeBankedMoney(withdraw);
                                        PlayerUtils.safeDeposit(p, withdraw);
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