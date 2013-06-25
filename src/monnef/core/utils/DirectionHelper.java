/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraftforge.common.ForgeDirection;

public class DirectionHelper {
    public static boolean isYAxis(ForgeDirection dir) {
        return dir == ForgeDirection.UP || dir == ForgeDirection.DOWN;
    }

    public static Integer[] applyRotations(Integer[] input, Integer... axisList) {
        checkDirectionArray(input, true);
        checkDirectionArray(axisList, false);
        Integer[] result = new Integer[6];

        for (int i = 0; i < input.length; i++) {
            ForgeDirection tmp = ForgeDirection.getOrientation(input[i]);
            for (int axis : axisList) {
                tmp = tmp.getRotation(ForgeDirection.getOrientation(axis));
            }
            result[i] = tmp.ordinal();
        }

        return result;
    }

    public static Integer[] applyRotationsInverted(Integer[] input, Integer... axisList) {
        Integer[] invertedRotations = new Integer[axisList.length];
        int j = 0;
        for (int i = axisList.length - 1; i >= 0; i--) {
            invertedRotations[j++] = ForgeDirection.getOrientation(axisList[i]).getOpposite().ordinal();
        }

        return applyRotations(input, invertedRotations);
    }

    public static void checkDirectionArray(Integer[] toTest, boolean expectSix) {
        if (expectSix && toTest.length != 6) {
            throw new RuntimeException("expected length six");
        }

        for (Integer i : toTest) {
            if (i < 0 || i > 5) {
                throw new RuntimeException("invalid side: " + i);
            }
        }
    }

    public static int opposite(int dir) {
        return ForgeDirection.getOrientation(dir).getOpposite().ordinal();
    }
}
