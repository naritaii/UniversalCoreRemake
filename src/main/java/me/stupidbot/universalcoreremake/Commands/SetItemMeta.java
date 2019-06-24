package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemMetadata;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

class SetItemMeta {
    @SuppressWarnings("SameReturnValue")
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

                    if (i != null) {
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
                                            TextUtils.capitalizeFully(i.getType().toString())));
                        } else
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&c" + TextUtils.capitalizeFully(i.getType().toString()) +
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

                if (i != null) {
                    p.setItemInHand(ItemMetadata.setMeta(i, args[0], args[1]));

                    Map<String, String> metaMap = ItemMetadata.getMeta(i);
                    StringBuilder metas = new StringBuilder();
                    for (String meta : metaMap.keySet()) {
                        if (metas.length() != 0)
                            metas.append(", ");
                        metas.append(meta)
                        .append(":")
                        .append(metaMap.get(meta));
                    }
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&a" + TextUtils.capitalizeFully(i.getType().toString()) +
                                    " now has the following tags: &e" + metas));
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou're not holding any items!"));
            }
        else  s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cOnly players may use this command!"));

        return true;
    }
}