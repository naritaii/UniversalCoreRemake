package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
                Material m = i.getType();

                if (m != Material.AIR) {
                    Map<String, String> metaMap = ItemMetadata.getMeta(i);
                    if (metaMap != null) {
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
                                        " has the following metadata: &e" + metas));
                    } else
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&a" + TextUtils.capitalizeFully(m.toString()) +
                                        " has no custom metadata!"));
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou're not holding any items!"));
            }
        else s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cOnly players may use this command!"));

        return true;
    }
}