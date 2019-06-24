package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.Utilities.ItemUtilities.ItemMetadata;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

class ReadItemMeta {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s instanceof Player)
            if (!s.hasPermission("universalcore.admin"))
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cYou don't have permission to use this command!"));
            else {
                Player p = (Player) s;
                ItemStack i = p.getItemInHand();

                if (i != null) {
                    Map<String, String> metaMap = ItemMetadata.getMeta(i);
                    if (metaMap != null) {
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
                                        " has the following meta &e" + metas));
                    } else
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&a" + TextUtils.capitalizeFully(i.getType().toString()) +
                                        " has no custom metadata!"));
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou're not looking at a block!"));
            }
        else s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cOnly players may use this command!"));

        return true;
    }
}