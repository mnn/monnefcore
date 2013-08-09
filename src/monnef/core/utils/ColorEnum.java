/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import java.util.HashMap;

public enum ColorEnum {
    WHITE(255, 255, 255),
    BLACK(0, 0, 0),
    RED(255, 0, 0),
    YELLOW(255, 255, 0),
    BLUE(0, 0, 255),
    LIGHT_BLUE(0, 255, 255),
    GREEN(0, 255, 0),;

    private final ColorHelper.IntColor color;
    private static HashMap<Integer, String> titles = new HashMap<Integer, String>();

    static {
        for (ColorEnum item : ColorEnum.values()) {
            titles.put(item.getInt(), item.toString().replace('_', ' ').toLowerCase());
        }
    }

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

    public static String getTitle(int intColor) {
        return titles.get(intColor);
    }
}
