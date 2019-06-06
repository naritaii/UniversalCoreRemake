package me.stupidbot.universalcoreremake.Utilities;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class PlayerUtils {
    public static Block getTargetBlock(Player p, int range) { // org.bukkit.entity.LivingEntity.getTargetBlock(...); is deprecated
        BlockIterator iter = new BlockIterator(p, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR)
                continue;
            break;
        }

        return lastBlock;
    }
}