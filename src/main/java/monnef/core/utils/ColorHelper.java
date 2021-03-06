/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

public class ColorHelper {
    public static int getInt(ColorEnum color) {
        return color.getInt();
    }

    public static int getInt(IntColor color) {
        return color.toInt();
    }

    public static int getInt(int red, int green, int blue) {
        return getInt(red, green, blue, 255);
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

    public static IntColor getColor(ColorEnum color) {
        return color.getColor();
    }

    public static IntColor getColor(int color) {
        IntColor res = new IntColor();
        getColor(color, res);
        return res;
    }

    public static void getColor(int color, IntColor out) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        out.setAll(red, green, blue, alpha);
    }

    public static int addBrightness(int inputColor, int amount) {
        IntColor c = getColor(inputColor);
        c.setRed(c.getRed() + amount);
        c.setGreen(c.getGreen() + amount);
        c.setBlue(c.getBlue() + amount);
        return getInt(c);
    }

    public static int addContrast(int inputColor, float modifier) {
        IntColor c = getColor(inputColor);
        c.setRed(Math.round(c.getRed() * modifier));
        c.setGreen(Math.round(c.getGreen() * modifier));
        c.setBlue(Math.round(c.getBlue() * modifier));
        return getInt(c);
    }

    public static class IntColor {
        private int red;
        private int green;
        private int blue;
        private int alpha;
        private boolean dirty = true;
        private int cachedIntValue;

        public IntColor() {
        }

        public IntColor(int color) {
            getColor(color, this);
        }

        public IntColor(int red, int green, int blue) {
            this(red, green, blue, (short) 255);
        }

        public IntColor(int red, int green, int blue, int alpha) {
            this.setRed(red);
            this.setGreen(green);
            this.setBlue(blue);
            this.setAlpha(alpha);
        }

        // TODO: cache float values

        public float getFloatRed() {
            return toFloat(getRed());
        }

        public float getFloatGreen() {
            return toFloat(getGreen());
        }

        public float getFloatBlue() {
            return toFloat(getBlue());
        }

        public float getFloatAlpha() {
            return toFloat(getAlpha());
        }

        private float toFloat(int value) {
            return value / 255f;
        }

        public int toInt() {
            if (dirty) {
                cachedIntValue = getInt(getRed(), getGreen(), getBlue(), getAlpha());
            }
            return cachedIntValue;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            dirty = true;
            this.red = red;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            dirty = true;
            this.green = green;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            dirty = true;
            this.blue = blue;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            dirty = true;
            this.alpha = alpha;
        }

        @Override
        public String toString() {
            return String.format("%d %d %d %d", getRed(), getGreen(), getBlue(), getAlpha());
        }

        public String formatHexWithAlpha() {
            return String.format("%s%2X", formatHex(), getAlpha());
        }

        public String formatHex() {
            return String.format("#%2X%2X%2X", getRed(), getGreen(), getBlue());
        }

        public void setAll(int red, int green, int blue, int alpha) {
            dirty = true;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public String formatTextOrHex() {
            String res = formatText();
            if (res == null) res = formatHex();
            return res;
        }

        public String formatText() {
            String res = ColorEnum.getTitle(toInt());
            if (res != null) return res;
            return DyeHelper.getDyeColorTitle(DyeHelper.getDyeIndexFromIntColor(toInt()));      // TODO: handle failure
        }
    }
}
