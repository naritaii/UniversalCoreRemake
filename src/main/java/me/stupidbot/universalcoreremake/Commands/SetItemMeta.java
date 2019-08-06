package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemMetadata;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

class SetItemMeta {
    @SuppressWarnings({"SameReturnValue", "ConstantConditions"})
    boolean execute(CommandSender s, String label, String[] args) {
        if (s instanceof Player)
            if (!s.hasPermission("universalcore.admin"))
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cYou don't have permission to use this command!"));
            else if (args.length == 0)
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " <remove|<metadata> <value>>"));
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("remove")) {
                    Player p = (Player) s;
                    ItemStack i = p.getItemInHand();
                    Material m = i.getType();

                    if (m != Material.AIR) {
                        Map<String, String> metaMap = ItemMetadata.getMeta(i);

                        if (metaMap != null) {
                            p.setItemInHand(ItemMetadata.removeAllMeta(i));

                            StringBuilder metas = new StringBuilder();
                            for (String meta : metaMap.keySet()) {
                                if (metas.length() != 0)
                                    metas.append(", ");
                                metas.append(meta)
                                .append(":")
                                .append(metaMap.get(meta));
                            }
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&aSuccessfully removed &e" + metas + " &afrom " +
                                            TextUtils.capitalizeFully(m.toString())));
                        } else
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&c" + TextUtils.capitalizeFully(m.toString()) +
                                            " already had no custom metadata!"));
                    } else
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cYou're not holding any items!"));
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " <metadata> <value>"));
            } else {
                Player p = (Player) s;
                ItemStack i = p.getItemInHand();
                Material m = i.getType();

                if (m != Material.AIR) {
                    StringBuilder value = new StringBuilder();
                    for (int t = 1; t < args.length; t++) {
                        if (t > 1)
                            value.append(" ");
                        value.append(args[t]);
                    }
                    p.setItemInHand(ItemMetadata.setMeta(i, args[0], value.toString()));

                    Map<String, String> metaMap = ItemMetadata.getMeta(i);
                    StringBuilder metas = new StringBuilder();
                    for (String meta : metaMap.keySet()) {
                        if (metas.length() != 0)
                            metas.append("&r&e, ");
                        metas.append(meta)
                        .append(":")
                        .append(metaMap.get(meta));
                    }
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&a" + TextUtils.capitalizeFully(m.toString()) +
                                    " now has the following metadata: &e" + metas));
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou're not holding any items!"));
            }
        else  s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cOnly players may use this command!"));

        return true;
    }
}