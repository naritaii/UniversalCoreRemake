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
            String type = split[0].toUpperCase().trim();
            StringBuilder arg = new StringBuilder();
            for (String args : Arrays.copyOfRange(split, 1, split.length))
                arg.append(" ").append(args);

            switch (type) {
                case "MONEY":
                    try {
                        r[i] = ChatColor.translateAlternateColorCodes('&',
                                "&6$" + TextUtils.addCommas(Double.parseDouble(arg.toString().trim())));
                    } catch (NumberFormatException e) {
                        r[i] = ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse money " + arg.toString().trim());
                    }
                    break;

                case "ITEM":
                    ItemStack is = ItemUtils.deserializeItemStack(arg.toString().trim());
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
                                "&cCould not parse item " + arg.toString().trim());
                    break;

                case "MESSAGE":
                    r[i] = ChatColor.translateAlternateColorCodes('&', arg.toString().trim());
                    break;

                case "XP":
                case "EXP":
                    try {
                        r[i] = ChatColor.translateAlternateColorCodes('&',
                                "&bXP " + TextUtils.addCommas(Integer.parseInt(arg.toString().trim())));
                    } catch (NumberFormatException e) {
                        r[i] = ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse " + s.toLowerCase() + " " + arg.toString().trim());
                    }
                    break;


                case "QUEST":
                case "SCRIPT":
                case "NONE":
                case "NULL":
                    r[i] = null;
                    break;

                default:
                    r[i] = ChatColor.translateAlternateColorCodes('&',
                            "&cCould not parse " + s);
                    break;
            }

            i++;
        }

        boolean allNull = true;
        for (String sr : r)
            if (sr != null) {
                allNull = false;
                break;
            }

        if (!allNull)
            return r;
        else
            return null;
    }

    public void give(Player p) {
        for (String s : rewards) {
            String[] split = s.split(" ");
            String type = split[0].toUpperCase().trim();
            StringBuilder arg = new StringBuilder();
            for (String args : Arrays.copyOfRange(split, 1, split.length))
                arg.append(" ").append(args);
            arg.trimToSize();

            switch (type) {
                case "MONEY":
                    try {
                        double d = Double.parseDouble(arg.toString().trim());
                        UniversalCoreRemake.getEconomy().depositPlayer(p, d);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&6$" + TextUtils.addCommas(d) + "&a has been added to your account."));
                    } catch (NumberFormatException e) {
                        p.sendMessage((ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse money " + arg.toString().trim())));
                    }
                    break;

                case "ITEM":
                    ItemStack is = ItemUtils.deserializeItemStack(arg.toString().trim());
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
                                "&cCould not parse item " + arg.toString().trim())));
                    break;

                case "QUEST":
                    UniversalObjectiveManager ubm = UniversalCoreRemake.getUniversalObjectiveManager();
                    if (ubm.registeredObjectivesDictionary.containsKey(arg.toString().trim())) {
                        UniversalObjective uo = ubm.registeredObjectives.get(ubm.registeredObjectivesDictionary.get(arg.toString().trim()));
                        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
                        if (up.getCompletedObjectives().contains(uo.getId()))
                            up.removeCompletedObjective(uo.getId());
                        else if (up.getSelectedObjectives().contains(uo.getId())) {
                            up.removeSelectedObjective(uo.getId());
                            up.removeObjectiveData(uo.getId());
                        }
                        up.addSelectedObjective(uo.getId());
                        uo.addPlayer(p);
                        UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, UniversalCoreRemake.getUniversalObjectiveManager().getNeeded(uo));
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse quest " + arg.toString().trim()));
                    }
                    break;

                case "MESSAGE":
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', arg.toString().trim()));
                    break;

                case "SCRIPT":
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ex run " + arg.toString().trim() + " player:p@" + p.getName());
                    break;

                case "XP":
                case "EXP":
                    try {
                        int i = Integer.parseInt(arg.toString().trim());
                        PlayerLevelling.giveXp(p, i);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&b+" + TextUtils.addCommas(i) + " XP"));
                    } catch (NumberFormatException e) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse " + s.toLowerCase() + " " + arg.toString().trim()));
                    }
                    break;

                case "NONE":
                case "NULL":
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
            String type = split[0].toUpperCase().trim();
            StringBuilder arg = new StringBuilder();
            for (String args : Arrays.copyOfRange(split, 1, split.length))
                arg.append(" ").append(args);

            switch (type) {
                case "MONEY":
                    try {
                        double d = Double.parseDouble(arg.toString().trim());
                        UniversalCoreRemake.getEconomy().depositPlayer(p, d);
                    } catch (NumberFormatException e) {
                        p.sendMessage((ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse money " + arg.toString().trim())));
                    }
                    break;

                case "ITEM":
                    ItemStack is = ItemUtils.deserializeItemStack(arg.toString().trim());
                    if (is != null)
                        ItemUtils.addItemSafe(p, is);
                    else
                        p.sendMessage((ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse item " + arg.toString().trim())));
                    break;

                case "QUEST":
                    UniversalObjectiveManager ubm = UniversalCoreRemake.getUniversalObjectiveManager();
                    if (ubm.registeredObjectivesDictionary.containsKey(arg.toString().trim())) {
                        UniversalObjective uo = ubm.registeredObjectives.get(ubm.registeredObjectivesDictionary.get(arg.toString().trim()));
                        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
                        if (up.getCompletedObjectives().contains(uo.getId()))
                            up.removeCompletedObjective(uo.getId());
                        else if (up.getSelectedObjectives().contains(uo.getId())) {
                            up.removeSelectedObjective(uo.getId());
                            up.removeObjectiveData(uo.getId());
                        }
                        up.addSelectedObjective(uo.getId());
                        uo.addPlayer(p);
                        UniversalObjectiveStartEvent event = new UniversalObjectiveStartEvent(p, uo, UniversalCoreRemake.getUniversalObjectiveManager().getNeeded(uo));
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse quest " + arg.toString().trim()));
                    }
                    break;

                case "SCRIPT":
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ex run " + arg.toString().trim() + " player:p@" + p.getName());
                    break;

                case "XP":
                case "EXP":
                    try {
                        PlayerLevelling.giveXp(p, Integer.parseInt(arg.toString().trim()));
                    } catch (NumberFormatException e) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cCould not parse " + s.toLowerCase() + " " + arg.toString().trim()));
                    }

                case "MESSAGE":
                case "NONE":
                case "NULL":
                    break;

                default:
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cCould not parse " + s));
                    break;
            }
        }
    }
}