package me.stupidbot.universalcoreremake.utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        return ((b.getX() & 0xFFF) << 20)       // localized so it should be OK.
                | ((b.getZ() & 0xFFF) << 8)
                | (b.getY() & 0xFF);
    }

/*    public static Location faceEntity(Location location, Entity entity) {
        Vector direction = location.toVector().subtract(entity.getLocation().toVector());
        direction.multiply(-1);
        location.setDirection(direction);
        return location;
    }*/

    public static Location center(Location loc) {
        return loc.add(loc.getX() < 0 ? 0.5 : -0.5,
                0.5,
                loc.getZ() < 0 ? 0.5 : -0.5);
    }

    public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
        // Gravity of a potion
        double gravity = 0.115;

        // Block locations
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(distanceSquared(from, to));

        // Height gain

        double maxGain = heightGain > (endGain + heightGain) ? heightGain : (endGain + heightGain);

        // Solve quadratic equation for velocity
        double a = -horizDist * horizDist / (4 * maxGain);
        double c = -endGain;

        double slope = -horizDist / (2 * a) - Math.sqrt(horizDist * horizDist - 4 * a * c) / (2 * a);

        // Vertical velocity
        double vy = Math.sqrt(maxGain * gravity);

        // Horizontal velocity
        double vh = vy / slope;

        // Calculate horizontal direction
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;

        // Horizontal velocity components
        double vx = vh * dirx;
        double vz = vh * dirz;

        return new Vector(vx, vy, vz);
    }

    private static double distanceSquared(Vector from, Vector to) {
        double dx = to.getBlockX() - from.getBlockX();
        double dz = to.getBlockZ() - from.getBlockZ();

        return dx * dx + dz * dz;
    }
}