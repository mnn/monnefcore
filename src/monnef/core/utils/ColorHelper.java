/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

public class ColorHelper {
    public static int getInt(int red, int green, int blue) {
        return getInt(red, green, blue, 0);
    }

    public static int getInt(int red, int green, int blue, int alpha) {
        red = clampByte(red);
        green = clampByte(green);
        blue = clampByte(blue);
        alpha = clampByte(alpha);
        return (alpha << 24) + (red << 16) + (green << 8) + blue;
    }

    public static int clampByte(int input) {
        if (input < 0) return 0;
        if (input > 255) return 255;
        return input;
    }

    public static IntColor getColor(int color) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        return new IntColor(red, green, blue, alpha);
    }

    public static class IntColor {
        public int red;
        public int green;
        public int blue;
        public int alpha;

        public IntColor() {
        }

        public IntColor(int red, int green, int blue) {
            this(red, green, blue, (short) 0);
        }

        public IntColor(int red, int green, int blue, int alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }
}
