/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class WorldHelper {
    public static final int WORLD_HEIGHT = 256;

    /**
     * Searches for a given block and fills matching coordinates.
     *
     * @param res        Results
     * @param w          World
     * @param x          X coordinate of centre of bottom box
     * @param y          Y coordinate of centre of bottom box
     * @param z          Z coordinate of centre of bottom box
     * @param radius     Radius of box
     * @param bottomSize Bottom padding size (-1 for ~y)
     * @param topSize    Top padding size (-1 for to the top of a world)
     * @param block      Block to look for
     * @param metadata   Metadata of block to look for (-1 denotes any metadata)
     */
    public static void getBlocksInBox(List<IntegerCoordinates> res, World w, int x, int y, int z, int radius, int bottomSize, int topSize, Block block, int metadata) {
        if (bottomSize == -1) bottomSize = y;
        if (topSize == -1) topSize = WORLD_HEIGHT - y;

        for (int ax = x - radius; ax <= x + radius; ax++)
            for (int ay = y - bottomSize; ay <= y + topSize; ay++)
                for (int az = z - radius; az <= z + radius; az++) {
                    Block currBlock = w.getBlock(ax, ay, az);
                    if (currBlock == block) {
                        if (metadata == -1 || w.getBlockMetadata(ax, ay, az) == metadata) {
                            res.add(new IntegerCoordinates(ax, ay, az, w));
                        }
                    }
                }
    }

    public static void dropBlockAsItemDo(World world, int x, int y, int z, Block block, int meta, int count) {
        if (world.isRemote) return;
        ItemStack stack = new ItemStack(block, count, meta);
        EntityItem item = new EntityItem(world, x + 0.5f, y + 0.5f, z + 0.5f, stack);
        world.spawnEntityInWorld(item);
    }

    public static void createExplosion(World world, Entity exploder, double x, double y, double z, float size, boolean flaming, boolean smoking) {
        world.newExplosion(exploder, x, y, z, size, flaming, smoking);
    }

    public static void dropItem(World world, int x, int y, int z, ItemStack stack) {
        if (world.isRemote) return;
        EntityItem i = new EntityItem(world, x, y, z, stack);
        i.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(i);
    }
}
