package me.stupidbot.universalcoreremake.commands;

import com.google.common.base.Joiner;
import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class Mutate {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s, String label, String[] args) {
        if (s.hasPermission("universalcore.admin"))
            if (s instanceof Player)
                if (args.length == 0)
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cInvalid usage! /" + label + " <enchant> [level]"));
                else {
                    Player p = (Player) s;
                    ItemStack i = p.getItemInHand();
                    if (i != null) {
                        Enchantment ench = null;
                        for (Enchantment e : UniversalEnchantment.ENCHANTMENTS)
                            if (args[0].equalsIgnoreCase(e.getName().replaceAll("\\s", "_"))) {
                                ench = e;
                                break;
                            }
                        if (ench != null) {
                            int lvl = 1;
                            if (args.length > 1)
                                try {
                                    lvl = Integer.parseInt(args[1]);
                                } catch (NumberFormatException e) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                            "&6" + args[1] + "&c is not a valid number!"));
                                }

                            i.addUnsafeEnchantment(ench, lvl);
                            p.setItemInHand(i);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&a" + ench.getName() + " " + lvl + " successfully added!"));
                        } else {
                            List<String> ids = UniversalEnchantment.ENCHANTMENTS.stream()
                                    .map((e) -> e.getName().replaceAll("\\s", "_")).collect(Collectors.toList());
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&c" + args[0] + " is not a valid enchantment! Try " +
                                            Joiner.on(", ").join(ids.subList(0, ids.size() - 1))
                                                    .concat(", or ").concat(ids.get(ids.size() - 1))));
                        }
                    } else
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cYou're not holding any items!"));
                }
            else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cOnly players may use this command!"));
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}
