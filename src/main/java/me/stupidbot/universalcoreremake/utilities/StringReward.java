package me.stupidbot.universalcoreremake.utilities;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.events.universalobjective.UniversalObjectiveStartEvent;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjective;
import me.stupidbot.universalcoreremake.managers.universalobjective.UniversalObjectiveManager;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import me.stupidbot.universalcoreremake.utilities.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class StringReward {
    private final String[] rewards;

    public StringReward(String... rewards) {
        this.rewards = rewards;
    }

    public String[] getRewards() {
        return this.rewards;
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
                                "&6$" + TextUtils.addCommas(Double.parseDouble(arg.toString())));
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

                    case "QUEST":
                        r[i] = null;
                        break;

                case "MESSAGE":
                    r[i] = ChatColor.translateAlternateColorCodes('&', arg.toString());

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
                        double d = Double.parseDouble(arg.toString());
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

                case "QUEST":
                    UniversalObjectiveManager ubm = UniversalCoreRemake.getUniversalObjectiveManager();
                    UniversalObjective uo = ubm.registeredObjectives.get(ubm.registeredObjectivesDictionary.get(arg.toString()));
                    uo.addPlayer(p);
                    UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
                    up.addSelectedObjective(uo.getId());
                    UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, UniversalCoreRemake.getUniversalObjectiveManager().getNeeded(uo));
                    Bukkit.getServer().getPluginManager().callEvent(event); // TODO Error Handling and chat message
                    break;

                case "MESSAGE":
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', arg.toString()));
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
                        double d = Double.parseDouble(arg.toString());
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

                case "QUEST":
                    UniversalObjectiveManager ubm = UniversalCoreRemake.getUniversalObjectiveManager();
                    UniversalObjective uo = ubm.registeredObjectives.get(ubm.registeredObjectivesDictionary.get(arg.toString()));
                    uo.addPlayer(p);
                    UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
                    up.addSelectedObjective(uo.getId());
                    UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, UniversalCoreRemake.getUniversalObjectiveManager().getNeeded(uo));
                    Bukkit.getServer().getPluginManager().callEvent(event); // TODO Error Handling
                    break;

                case "MESSAGE":
                    break;

                default:
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cCould not parse " + s));
                    break;
            }
        }
    }
}