/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class BoxHelper {
    public static AxisAlignedBB createSquareFromCenter(double radius, ForgeDirection direction, double length) {
        double x1 = -radius, x2 = radius;
        double y1 = -radius, y2 = radius;
        double z1 = -length / 2, z2 = length / 2;
        switch (direction) {
            case NORTH:
            case SOUTH:
                return AxisAlignedBB.getBoundingBox(x1, y1, z1, x2, y2, z2);

            case WEST:
            case EAST:
                return AxisAlignedBB.getBoundingBox(z1, y1, x1, z2, y2, x2);
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

    public static AxisAlignedBB getNonPooledAABB(AxisAlignedBB template) {
        if (template == null) return null;
        return AxisAlignedBB.getBoundingBox(template.minX, template.minY, template.minZ, template.maxX, template.maxY, template.maxZ);
    }
}
