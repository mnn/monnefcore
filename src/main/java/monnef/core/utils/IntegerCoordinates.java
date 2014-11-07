/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import monnef.core.api.IIntegerCoordinates;
import monnef.core.mod.MonnefCoreNormalMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static monnef.core.utils.MathHelper.square;
import static net.minecraft.util.MathHelper.sqrt_float;

public class IntegerCoordinates implements IIntegerCoordinates {
    // do not ever change after construction!
    protected int x;
    protected int y;
    protected int z;

    protected String compoundTagName;
    private int dimension;
    private boolean locked = false;
    private static boolean ignoreNullWorld = false;

    public IntegerCoordinates(TileEntity tile) {
        setWorld(tile.getWorldObj());
        x = tile.xCoord;
        y = tile.yCoord;
        z = tile.zCoord;

        postInit();
    }

    public IntegerCoordinates(int x, int y, int z, World world) {
        setWorld(world);
        this.x = x;
        this.y = y;
        this.z = z;

        postInit();
    }

    public IntegerCoordinates(NBTTagCompound tag, String compoundTagName) {
        this.compoundTagName = compoundTagName;
        loadFrom(tag, compoundTagName);

        postInit();
    }

    public IntegerCoordinates(World world, MovingObjectPosition pos) {
        setWorld(world);
        x = pos.blockX;
        y = pos.blockY;
        z = pos.blockZ;
    }

    // used for testing
    private IntegerCoordinates(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        ignoreNullWorld = true;
        postInit();
    }

    private void postInit() {
        if (!locked) {
            locked = true;
        }
    }

    @Override
    public TileEntity getTile() {
        return getWorld().getTileEntity(x, y, z);
    }

    @Override
    public World getWorld() {
        if (ignoreNullWorld) {
            // performance?
            return null;
        }
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension);
        }
        return MonnefCoreNormalMod.proxy.getClientWorld();
    }

    private void setWorld(World world) {
        if (world == null) {
            if (!ignoreNullWorld) {
                throw new NullPointerException("world");
            }
        } else {
            this.dimension = world.provider.dimensionId;
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void saveTo(NBTTagCompound tag, String tagName) {
        NBTTagCompound save = new NBTTagCompound();
        save.setInteger("x", getX());
        save.setInteger("y", getY());
        save.setInteger("z", getZ());
        save.setInteger("dim", getWorld().provider.dimensionId);
        tag.setTag(tagName, save);
    }

    @Override
    public void loadFrom(NBTTagCompound tag, String tagName) {
        if (locked) {
            throw new RuntimeException("locked");
        }

        NBTTagCompound save = tag.getCompoundTag(tagName);
        x = save.getInteger("x");
        y = save.getInteger("y");
        z = save.getInteger("z");
        dimension = save.getInteger("dim");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerCoordinates that = (IntegerCoordinates) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        if (dimension != that.dimension) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + dimension;
        return result;
    }

    public float computeDistance(IIntegerCoordinates other) {
        return sqrt_float(computeDistanceSquare(other));
    }

    public float computeDistanceSquare(IIntegerCoordinates other) {
        return square(getX() - other.getX()) + square(getY() - other.getY()) + square(getZ() - other.getZ());
    }

    @Override
    public IIntegerCoordinates shiftInDirectionBy(ForgeDirection dir, int amount) {
        return new IntegerCoordinates(getX() + dir.offsetX * amount, getY() + dir.offsetY * amount, getZ() + dir.offsetZ * amount, getWorld());
    }

    @Override
    public TileEntity getBlockTileEntity() {
        return getWorld().getTileEntity(getX(), getY(), getZ());
    }

    @Override
    public Block getBlock() {
        return getWorld().getBlock(getX(), getY(), getZ());
    }

    @Override
    public int getBlockMetadata() {
        return getWorld().getBlockMetadata(getX(), getY(), getZ());
    }

    @Override
    public int getRedstoneWirePowerLevel() {
        return getBlock() == Blocks.redstone_wire ? getBlockMetadata() : 0;
    }

    @Override
    public int getIndirectPowerFromSide(int side) {
        return getWorld().getIndirectPowerLevelTo(getX(), getY(), getZ(), side);
    }

    @Override
    public IIntegerCoordinates strafeInDirection(ForgeDirection direction, int amount) {
        return shiftInDirectionBy(direction.getRotation(ForgeDirection.DOWN), amount);
    }

    @Override
    public IIntegerCoordinates applyRelativeCoordinates(ForgeDirection rotation, int rx, int ry, int rz) {
        return shiftInDirectionBy(rotation, rz).shiftInDirectionBy(ForgeDirection.UP, ry).strafeInDirection(rotation, rx);
    }

    @Override
    public IIntegerCoordinates copy() {
        return new IntegerCoordinates(getX(), getY(), getZ(), getWorld());
    }

    @Override
    public String format() {
        return String.format("%d@%dx%dx%d", getWorld().provider.dimensionId, getX(), getY(), getZ());
    }

    @Override
    public boolean isAir() {
        return getWorld().isAirBlock(getX(), getY(), getZ());
    }

    @Override
    public void setBlock(Block block) {
        BlockHelper.setBlock(getWorld(), getX(), getY(), getZ(), block);
    }

    @Override
    public void setMetadata(int meta) {
        BlockHelper.setBlockMetadata(getWorld(), getX(), getY(), getZ(), meta);
    }

    @Override
    public IIntegerCoordinates move(int x, int y, int z) {
        return new IntegerCoordinates(getX() + x, getY() + y, getZ() + z, getWorld());
    }
}
