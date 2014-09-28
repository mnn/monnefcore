/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

public enum DyeColor {
    // "black", "red", "green", "brown",
    // "blue", "purple", "cyan", "silver",
    // "gray", "pink", "lime", "yellow",
    // "lightBlue", "magenta", "orange", "white"

    BLACK("black"), RED("red"), GREEN("green"), BROWN("brown"), BLUE("blue"), PURPLE("purple"), CYAN("cyan"), SILVER("silver"),
    GRAY("gray"), PINK("pink"), LIME("lime"), YELLOW("yellow"), L_BLUE("lightBlue"), MAGENTA("magenta"), ORANGE("orange"), WHITE("white");

    private final String colorName;
    public static final DyeColor[] DYE = {BLACK, RED, GREEN, BROWN, BLUE, PURPLE, CYAN, SILVER, GRAY, PINK, LIME, YELLOW, L_BLUE, MAGENTA, ORANGE, WHITE};

    DyeColor(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }
}
