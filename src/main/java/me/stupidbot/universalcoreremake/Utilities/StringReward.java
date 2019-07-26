package me.stupidbot.universalcoreremake.Utilities;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class StringReward {
    private final String[] rewards;

    public StringReward(String... rewards) {
        this.rewards = rewards;
    }

    public String[] asStrings() {
        String[] r = new String[rewards.length];
        int i = 0;
        for (String s : rewards) {
            String[] split = s.split(" ");
            String type = split[0].toUpperCase();
            StringBuilder arg = new StringBuilder();
            for (String args : Arrays.copyOfRange(split, 1, split.length))
                arg.append(args);

            switch (type) {
                case "MONEY":
                    try {
                        r[i] = ChatColor.translateAlternateColorCodes('&',
                                "&6$" + TextUtils.addCommas(Double.valueOf(arg.toString())));
                    } catch (NumberFormatException e) {
                        r[i] = ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse money " + arg.toString());
                    }
                    break;

                case "ITEM":
                    ItemStack is = ItemUtils.deserializeItemStack(arg.toString());
                    if (is != null)
                        if (is.hasItemMeta())
                            r[i] = ChatColor.translateAlternateColorCodes('&',
                                    "&3x" + is.getAmount() + "&a " +
                                            (is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() :
                                                    TextUtils.capitalizeFully(is.getType().toString())));
                        else
                            r[i] = TextUtils.capitalizeFully(is.getType().toString());
                    else
                        r[i] = ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse item " + arg.toString());
                    break;

                default:
                    r[i] = ChatColor.translateAlternateColorCodes('&',
                            "&cCould not parse " + s);
                    break;
            }

            i++;
        }

        return r;
    }

    public void give(Player p) {
        for (String s : rewards) {
            String[] split = s.split(" ");
            String type = split[0].toUpperCase();
            StringBuilder arg = new StringBuilder();
            for (String args : Arrays.copyOfRange(split, 1, split.length))
                arg.append(args);

            switch (type) {
                case "MONEY":
                    try {
                        double d = Double.valueOf(arg.toString());
                        UniversalCoreRemake.getEconomy().depositPlayer(p, d);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&6$" + TextUtils.addCommas(d) + "&a has been added to your account."));
                    } catch (NumberFormatException e) {
                        p.sendMessage((ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse money " + arg.toString())));
                    }
                    break;

                case "ITEM":
                    ItemStack is = ItemUtils.deserializeItemStack(arg.toString());
                    if (is != null)
                        if (is.hasItemMeta()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&3+x" + is.getAmount() + "&a " +
                                            (is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() :
                                                    TextUtils.capitalizeFully(is.getType().toString()))));
                            ItemUtils.addItemSafe(p, is);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&3+x" + is.getAmount() + "&a " +
                                            TextUtils.capitalizeFully(is.getType().toString())));
                            ItemUtils.addItemSafe(p, is);
                        }
                    else
                        p.sendMessage((ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse item " + arg.toString())));
                    break;

                default:
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cCould not parse " + s));
                    break;
            }
        }
    }

    public void giveNoMessage(Player p) {
        for (String s : rewards) {
            String[] split = s.split(" ");
            String type = split[0].toUpperCase();
            StringBuilder arg = new StringBuilder();
            for (String args : Arrays.copyOfRange(split, 1, split.length))
                arg.append(args);

            switch (type) {
                case "MONEY":
                    try {
                        double d = Double.valueOf(arg.toString());
                        UniversalCoreRemake.getEconomy().depositPlayer(p, d);
                    } catch (NumberFormatException e) {
                        p.sendMessage((ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse money " + arg.toString())));
                    }
                    break;

                case "ITEM":
                    ItemStack is = ItemUtils.deserializeItemStack(arg.toString());
                    if (is != null)
                        if (is.hasItemMeta()) {
                            ItemUtils.addItemSafe(p, is);
                        } else {
                            ItemUtils.addItemSafe(p, is);
                        }
                    else
                        p.sendMessage((ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse item " + arg.toString())));
                    break;

                default:
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cCould not parse " + s));
                    break;
            }
        }
    }
}