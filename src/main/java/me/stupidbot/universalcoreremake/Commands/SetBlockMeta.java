package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.PlayerUtils;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

class SetBlockMeta {
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
                    Block b = PlayerUtils.getTargetBlock(p, 6);
                    Material m = b.getType();

                    if (m != Material.AIR) {
                        HashMap<String, String> metaMap =
                                UniversalCoreRemake.getBlockMetadataManager().getAllMetadata(b);

                        if (metaMap != null) {
                            UniversalCoreRemake.getBlockMetadataManager().deleteAllMetadata(b);

                            StringBuilder metas = new StringBuilder();
                            for (String meta : metaMap.keySet()) {
                                if (metas.length() != 0)
                                    metas.append(", ");
                                metas.append(meta);
                                metas.append(":");
                                metas.append(metaMap.get(meta));
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
                Block b = PlayerUtils.getTargetBlock(p, 6);
                Material m = b.getType();

                if (m != Material.AIR) {
                    UniversalCoreRemake.getBlockMetadataManager().setMetadata(b, args[0], args[1]);

                    HashMap<String, String> metaMap = UniversalCoreRemake.getBlockMetadataManager().getAllMetadata(b);
                    StringBuilder metas = new StringBuilder();
                    for (String meta : metaMap.keySet()) {
                        if (metas.length() != 0)
                            metas.append(", ");
                        metas.append(meta);
                        metas.append(":");
                        metas.append(metaMap.get(meta));
                    }
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&a" + TextUtils.capitalizeFully(m.toString()) +
                                    " has now has the following tags: &e" + metas));
                } else
                    s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou're not looking at a block!"));
            }
        else  s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cOnly players may use this command!"));

        return true;
    }
}