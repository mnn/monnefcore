/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;


import monnef.core.ItemStackInInventory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

import java.util.Random;

public class InventoryUtils {
    private static Random rand = new Random();

    public static void dropItems(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack toDrop = inventory.getStackInSlot(i);

            if (toDrop != null && toDrop.stackSize > 0) {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                        toDrop.copy());

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                toDrop.stackSize = 0;
            }
        }
    }

    public static ItemStackInInventory findFirstMatchingItem(IInventory inventory, ItemStack needle) {
        int index = -1;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack curr = inventory.getStackInSlot(i);
            if (curr != null) {
                if (needle.isItemEqual(curr)) {
                    index = i;
                    break;
                }
            }
        }
        if (index == -1) return null;

        return new ItemStackInInventory(inventory, index);
    }

    /**
     * Tries to insert a stack into given inventory.
     *
     * @param inventory Target inventory.
     * @param stack     Item to be inserted.
     * @param side      Side.
     * @return Leftover item.
     */
    public static ItemStack insertStackToExternalInventory(IInventory inventory, ItemStack stack, int side) {
        return TileEntityHopper.func_145889_a(inventory, stack, side); // insertStack
    }
}
