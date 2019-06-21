package me.stupidbot.universalcoreremake.Utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class LocationUtils {
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

    public static Location faceEntity(Location location, Entity entity) {
        Vector direction = location.toVector().subtract(entity.getLocation().toVector());
        direction.multiply(-1);
        location.setDirection(direction);
        return location;
    }

    public static Location center(Location loc) {
        return loc.add(loc.getX() < 0 ? 0.5 : -0.5,
                0.5,
                loc.getZ() < 0 ? 0.5 : -0.5);
    }
}