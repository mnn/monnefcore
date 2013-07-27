/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import monnef.core.api.IIntegerCoordinates;

import java.text.DecimalFormat;

import static net.minecraft.util.MathHelper.ceiling_float_int;
import static net.minecraft.util.MathHelper.sqrt_float;

public class MathHelper {
    public static final DecimalFormat oneDecimalPlace = new DecimalFormat("#.#");

    public static int square(int number) {
        return number * number;
    }

    public static float exactDistance(IIntegerCoordinates a, IIntegerCoordinates b) {
        return sqrt_float(square(a.getX() - b.getX()) + square(a.getY() - b.getY()) + square(a.getZ() - b.getZ()));
    }

    public static int exactDistanceInt(IIntegerCoordinates a, IIntegerCoordinates b) {
        return ceiling_float_int(exactDistance(a, b));
    }

    public static float degToRad(float deg) {
        return (float) (deg * Math.PI / 180f);
    }

    public static int[] range(int start, int count) {
        return range(start, count, 1);
    }

    public static int[] range(int start, int count, int step) {
        int[] res = new int[count];
        for (int i = 0; i < count; i++) {
            res[i] = start + i * step;
        }
        return res;
    }
}
