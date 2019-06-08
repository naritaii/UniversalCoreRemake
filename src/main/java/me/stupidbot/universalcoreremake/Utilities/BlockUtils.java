package me.stupidbot.universalcoreremake.Utilities;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class BlockUtils {
    public static Block getTargetBlock(Player p, int range) { // org.bukkit.entity.LivingEntity.getTargetBlock(...);
        BlockIterator iter = new BlockIterator(p, range);     // is deprecated
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR)
                continue;
            break;
        }

        return lastBlock;
    }

    public static int getBlockEntityId(Block b) { // There will be some overlap here, but these effects are very
        return   ((b.getX() & 0xFFF) << 20)       // localized so it should be OK.
                | ((b.getZ() & 0xFFF) << 8)
                | (b.getY() & 0xFF);
    }
}