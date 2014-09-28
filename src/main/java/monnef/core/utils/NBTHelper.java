/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class NBTHelper {
    public static class TagTypes {
        public final static int TAG_End = 0;
        public final static int TAG_Byte = 1;
        public final static int TAG_Short = 2;
        public final static int TAG_Int = 3;
        public final static int TAG_Long = 4;
        public final static int TAG_Float = 5;
        public final static int TAG_Double = 6;
        public final static int TAG_Byte_Array = 7;
        public final static int TAG_String = 8;
        public final static int TAG_List = 9;
        public final static int TAG_Compound = 10;
        public final static int TAG_Int_Array = 11;
    }

    public static ItemStack init(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack;
    }

    public static IntegerCoordinates getCoords(ItemStack stack, String compoundName) {
        init(stack);
        NBTTagCompound data = stack.getTagCompound();

        return getCoords(data, compoundName);
    }

    private static IntegerCoordinates getCoords(NBTTagCompound data, String compoundName) {
        if (!data.hasKey(compoundName)) {
            return null;
        }

        try {
            NBTTagCompound compound = data.getCompoundTag(compoundName);
            int cx = compound.getInteger("x");
            int cy = compound.getInteger("y");
            int cz = compound.getInteger("z");
            int dimm = compound.getInteger("dimm");
            World world = DimensionManager.getWorld(dimm);

            return new IntegerCoordinates(cx, cy, cz, world);
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack setCoords(ItemStack stack, IntegerCoordinates coords, String compoundName) {
        init(stack);
        NBTTagCompound data = stack.getTagCompound();

        serCoords(data, coords, compoundName);
        return stack;
    }

    private static void serCoords(NBTTagCompound data, IntegerCoordinates coords, String compoundName) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("x", coords.getX());
        compound.setInteger("y", coords.getY());
        compound.setInteger("z", coords.getZ());
        compound.setInteger("dimm", coords.getWorld().provider.dimensionId);
        data.setTag(compoundName, compound);
    }

    public static boolean hasKey(ItemStack stack, String keyName) {
        init(stack);
        return stack.getTagCompound().hasKey(keyName);
    }
}
