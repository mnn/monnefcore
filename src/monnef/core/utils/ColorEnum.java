/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

public enum ColorEnum {
    WHITE(255, 255, 255),
    BLACK(0, 0, 0),
    RED(255, 0, 0),
    YELLOW(255, 255, 0),
    BLUE(0, 0, 255),
    LIGHT_BLUE(0, 255, 255),
    GREEN(0, 255, 0),;

    private final ColorHelper.IntColor color;

    ColorEnum(int r, int g, int b) {
        this(r, g, b, 255);
    }

    ColorEnum(int r, int g, int b, int a) {
        this.color = new ColorHelper.IntColor(r, g, b, a);
    }

    public ColorHelper.IntColor getColor() {
        return color;
    }

    public int getInt() {
        return color.toInt();
    }
}
