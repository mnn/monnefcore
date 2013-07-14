/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.HashSet;

public class BlockHelper {
    private static HashSet<Integer> winterBlocks = new HashSet<Integer>();

    static {
        winterBlocks.add(Block.snow.blockID);
        winterBlocks.add(Block.blockSnow.blockID);
        winterBlocks.add(Block.ice.blockID);
    }

    /* MCP comment
     * Flag 0x01 will pass the original block ID when notifying adjacent blocks, otherwise it will pass 0.
     * Flag 0x02 will trigger a block update both on server and on client.
     * Flag 0x04, if used with 0x02, will prevent a block update on client worlds.
     */
    public static final int NOTIFY_FLAG = 2;
    public static final int SEND_ID_OF_CHANGED_BLOCK_FLAG = 1;
    public static final int NOT_UPDATE_ON_CLIENT = 4;
    public static final int NOTIFY_NONE = 0;

    public static final int NOTIFY_ALL = NOTIFY_FLAG | SEND_ID_OF_CHANGED_BLOCK_FLAG;

    // with notify
    public static boolean setBlockMetadata(World world, int x, int y, int z, int metadata) {
        return world.setBlockMetadataWithNotify(x, y, z, metadata, NOTIFY_ALL);
    }

    // with notify
    public static boolean setBlock(World world, int x, int y, int z, int id) {
        return world.setBlock(x, y, z, id);
    }

    // with notify
    public static boolean setBlock(World world, int x, int y, int z, int id, int meta) {
        return world.setBlock(x, y, z, id, meta, NOTIFY_ALL);
    }

    public static boolean setBlockWithoutNotify(World world, int x, int y, int z, int id, int meta) {
        return world.setBlock(x, y, z, id, meta, NOTIFY_NONE);
    }

    public static boolean isWinterBlock(int bId) {
        return winterBlocks.contains(bId);
    }

    private static double getCurrentLocation(int coord, boolean addCurrentLocation) {
        return addCurrentLocation ? coord : 0;
    }

    public static AxisAlignedBB rotateBoundingBoxCoordinates(BoundingBoxSize box, int rotation, int x, int y, int z, boolean addCurrentLocation) {
        //return AxisAlignedBB.getAABBPool().getAABB((double) x + this.minX, (double) y + this.minY, (double) z + this.minZ, (double) x + this.maxX, (double) y + this.maxY, (double) z + this.maxZ);
        //return AxisAlignedBB.getAABBPool().getAABB((double) x + box.x1, (double) y + box.y1, (double) z + box.z1, (double) x + box.x2, (double) y + box.y2, (double) z + box.z2);

        double bbx = getCurrentLocation(x, addCurrentLocation) + box.x1;
        double bby = getCurrentLocation(y, addCurrentLocation) + box.y1;
        double bbz = getCurrentLocation(z, addCurrentLocation) + box.z1;
        double bbxx = getCurrentLocation(x, addCurrentLocation) + box.x2;
        double bbyy = getCurrentLocation(y, addCurrentLocation) + box.y2;
        double bbzz = getCurrentLocation(z, addCurrentLocation) + box.z2;
        switch (rotation) {
            case 0:
                return AxisAlignedBB.getAABBPool().getAABB(bbx, bby, bbz, bbxx, bbyy, bbzz);

            case 3:
                return AxisAlignedBB.getAABBPool().getAABB(bbz, bby, 1 - bbxx, bbzz, bbyy, 1 - bbx);

            case 2:
                return AxisAlignedBB.getAABBPool().getAABB(1 - bbxx, bby, 1 - bbzz, 1 - bbx, bbyy, 1 - bbz);

            case 1:
                return AxisAlignedBB.getAABBPool().getAABB(1 - bbzz, bby, bbx, 1 - bbz, bbyy, bbxx);

            default:
                return AxisAlignedBB.getAABBPool().getAABB(bbx - 1, bby, bbz - 1, bbxx + 1, bbyy, bbzz + 1);
        }

    }
}
