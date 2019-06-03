package me.stupidbot.universalcoreremake.Commands;

import me.stupidbot.universalcoreremake.Utilities.BlockMetadata;
import me.stupidbot.universalcoreremake.Utilities.Players.PlayerUtils;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

class SetBlockMeta {
    public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
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
                        HashMap<String, String> metaMap = BlockMetadata.getAllMetadata(b);

                        if (metaMap != null) {
                            BlockMetadata.deleteAllMetadata(b);

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
                    BlockMetadata.setMetadata(b, args[0], args[1]);

                    HashMap<String, String> metaMap = BlockMetadata.getAllMetadata(b);
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