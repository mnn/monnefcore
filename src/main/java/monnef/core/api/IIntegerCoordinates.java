/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.api;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IIntegerCoordinates {
    TileEntity getTile();

    World getWorld();

    int getX();

    int getY();

    int getZ();

    void saveTo(NBTTagCompound tag, String tagName);

    void loadFrom(NBTTagCompound tag, String tagName);

    IIntegerCoordinates shiftInDirectionBy(ForgeDirection dir, int amount);

    TileEntity getBlockTileEntity();

    Block getBlock();

    int getBlockMetadata();

    int getRedstoneWirePowerLevel();

    int getIndirectPowerFromSide(int side);

    IIntegerCoordinates strafeInDirection(ForgeDirection direction, int amount);

    IIntegerCoordinates applyRelativeCoordinates(ForgeDirection rotation, int rx, int ry, int rz);

    IIntegerCoordinates copy();

    String format();

    boolean isAir();
}
