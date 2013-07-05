/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCloth;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class DyeHelper {
    private static final BiMap<String, Integer> dyeNames;
    private static final BiMap<String, Integer> woolNames;

    static {
        dyeNames = HashBiMap.create();
        woolNames = HashBiMap.create();
        for (int i = 0; i < ItemDye.dyeColorNames.length; i++) {
            String colorName = ItemDye.dyeColorNames[i];
            dyeNames.put(colorName, i);
            woolNames.put(colorName, BlockCloth.getBlockFromDye(i));
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
        return new ItemStack(Item.dyePowder, 1, getDyeNum(color));
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

    public static String getWoolColorName(int color) {
        return woolNames.inverse().get(color);
    }

    public static String getDyeColorName(int color) {
        return dyeNames.inverse().get(color);
    }
}
