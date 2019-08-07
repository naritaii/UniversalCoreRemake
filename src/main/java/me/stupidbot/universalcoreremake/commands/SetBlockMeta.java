package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.LocationUtils;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

class SetBlockMeta {
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
                    Block b = LocationUtils.getTargetBlock(p, 6);
                    Material m = b.getType();

                    if (m != Material.AIR) {
                        Map<String, String> metaMap =
                                UniversalCoreRemake.getBlockMetadataManager().getMeta(b);

                        if (metaMap != null) {
                            UniversalCoreRemake.getBlockMetadataManager().removeAllMeta(b);

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
                                "&cYou're not looking at a block!"));
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cInvalid usage! /" + label + " <metadata> <value>"));
            } else {
                Player p = (Player) s;
                Block b = LocationUtils.getTargetBlock(p, 6);
                Material m = b.getType();

                if (m != Material.AIR) {
                    StringBuilder value = new StringBuilder();
                    for (int t = 1; t < args.length; t++) {
                        if (t > 1)
                            value.append(" ");
                        value.append(args[t]);
                    }
                    UniversalCoreRemake.getBlockMetadataManager().setMeta(b, args[0], value.toString());

                    Map<String, String> metaMap = UniversalCoreRemake.getBlockMetadataManager().getMeta(b);
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
                            "&cYou're not looking at a block!"));
            }
        else  s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cOnly players may use this command!"));

        return true;
    }
}