package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.LocationUtils;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

class ReadBlockMeta {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s instanceof Player)
            if (!s.hasPermission("universalcore.admin"))
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cYou don't have permission to use this command!"));
            else {
                Player p = (Player) s;
                Block b = LocationUtils.getTargetBlock(p, 6);
                Material m = b.getType();

                if (m != Material.AIR) {
                    Map<String, String> metaMap =
                            UniversalCoreRemake.getBlockMetadataManager().getMeta(b);
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
                                "&a" + TextUtils.capitalizeFully(m.toString()) +
                                        " has the following meta &e" + metas));
                    } else
                        s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&a" + TextUtils.capitalizeFully(m.toString()) +
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