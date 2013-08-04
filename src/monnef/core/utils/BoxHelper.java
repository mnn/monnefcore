/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

public class BoxHelper {
    public static AxisAlignedBB createSquareFromCenter(double radius, ForgeDirection direction) {
        double x1 = -radius, x2 = radius;
        double y1 = -radius, y2 = radius;
        double z1 = 0, z2 = 1;
        switch (direction) {
            case NORTH:
            case SOUTH:
                return AxisAlignedBB.getAABBPool().getAABB(x1, y1, z1, x2, y2, z2);

            case WEST:
            case EAST:
                return AxisAlignedBB.getAABBPool().getAABB(y1, x1, z1, y2, x2, z2);
        }

        return null;
    }

    public static void narrowFrontSide(AxisAlignedBB box, ForgeDirection direction, double amount) {
        switch (direction) {
            case NORTH:
                box.maxZ -= amount;
                break;

            case SOUTH:
                box.minZ += amount;
                break;

            case EAST:
                box.maxX -= amount;
                break;

            case WEST:
                box.minX += amount;
                break;

            case UP:
                box.minY += amount;
                break;

            case DOWN:
                box.maxY -= amount;
                break;
        }
    }
}
