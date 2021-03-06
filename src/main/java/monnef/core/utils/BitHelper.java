/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

public class BitHelper {
    public static boolean isBitSet(int a, int bitNumber) {
        return (a & (1 << bitNumber)) != 0;
    }

    public static int setBit(int a, int bitNumber) {
        return a | (1 << bitNumber);
    }

    public static int unsetBit(int a, int bitNumber) {
        return a & ~(1 << bitNumber);
    }

    public static int toggleBit(int a, int bitNumber) {
        return a ^ (1 << bitNumber);
    }

    public static int setBitToValue(int a, int bitNumber, boolean value) {
        if (value) {
            return setBit(a, bitNumber);
        } else {
            return unsetBit(a, bitNumber);
        }
    }

    public static int generateOnes(int count) {
        int o = 0;
        while (count-- > 0) {
            o = (o << 1) + 1;
        }
        return o;
    }
}
