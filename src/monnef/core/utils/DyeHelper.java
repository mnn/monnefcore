/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class DyeHelper {
    private static final BiMap<String, Integer> dyeNames;
    private static final BiMap<String, Integer> woolNames;
    private static final BiMap<Integer, Integer> intColorToDyeIndex;

    private static final int EXPECTED_SIZE = 16;

    static {
        dyeNames = HashBiMap.create(EXPECTED_SIZE);
        woolNames = HashBiMap.create(EXPECTED_SIZE);
        intColorToDyeIndex = HashBiMap.create(EXPECTED_SIZE);
        for (int i = 0; i < ItemDye.dyeColorNames.length; i++) {
            String colorName = ItemDye.dyeColorNames[i];
            dyeNames.put(colorName, i);
            woolNames.put(colorName, BlockColored.getBlockFromDye(i));
            intColorToDyeIndex.put(getIntColor(i), i);
        }
    }

    public static int getDyeNum(DyeColor color) {
        Integer index = dyeNames.get(color.getColorName());
        if (index == null) {
            throw new RuntimeException("unknown color name: " + color.getColorName());
        }
        return index;
    }

    public static int getWoolNum(DyeColor color) {
        Integer index = woolNames.get(color.getColorName());
        if (index == null) {
            throw new RuntimeException("unknown color name: " + color.getColorName());
        }
        return index;
    }

    public static ItemStack getDye(DyeColor color) {
        return getDye(getDyeNum(color));
    }

    public static ItemStack getDye(int color) {
        return new ItemStack(Item.dyePowder, 1, color);
    }

    public static ItemStack getWool(DyeColor color) {
        return new ItemStack(Block.cloth, 1, getWoolNum(color));
    }

    public static int getIntColor(DyeColor color) {
        return getIntColor(color.ordinal());
    }

    public static int getIntColor(int color) {
        return ItemDye.dyeColors[color];
    }

    public static String getWoolColorName(int index) {
        return woolNames.inverse().get(index);
    }

    public static String getDyeColorName(int index) {
        return dyeNames.inverse().get(index);
    }

    public static int getDyeIndexFromIntColor(int color) {
        return intColorToDyeIndex.get(color);
    }

    public static String getDyeColorTitle(int index) {
        String dyeName = getDyeColorName(index);
        if (dyeName == null) return null;
        return StringsHelper.makeFirstCapital(StringsHelper.insertSpaceOnLowerUpperCaseChange(dyeName).toLowerCase());
    }

    public static int getIndexFromIntColor(int intColor) {
        return intColorToDyeIndex.get(intColor);
    }
}
