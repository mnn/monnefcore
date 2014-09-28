/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

public class GuiHelper {
    public static final int COLOR_GRAY = ColorHelper.getInt(139, 139, 139);
    public static final int COLOR_DARK_GRAY = ColorHelper.getInt(55, 55, 55);
    public static final int COLOR_LIGHT_GRAY = ColorHelper.getInt(198, 198, 198);
    public static final int COLOR_WHITE = ColorHelper.getInt(255, 255, 255);

    public static void drawModalRectFromDown(GuiScreen gui, int x, int y, int textureX, int textureY, int width, int height, int heightMax) {
        // x, y, u, v, width, height
        int invHeight = heightMax - height;
        gui.drawTexturedModalRect(x, y + invHeight, textureX, textureY + invHeight, width, height);
    }

    public static void drawRect(int x, int y, int width, int height, ColorHelper.IntColor color) {
        drawRect(x, y, width, height, color.toInt());
    }

    public static void drawRect(int x, int y, int width, int height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void setTessellatorColor(Tessellator tessellator, int color) {
        setTessellatorColor(tessellator, ColorHelper.getColor(color));
    }

    public static void setTessellatorColor(Tessellator tessellator, ColorHelper.IntColor color) {
        tessellator.setColorRGBA_F(color.getFloatRed(), color.getFloatGreen(), color.getFloatBlue(), color.getFloatAlpha());
    }

    public static void drawPixel(int x, int y, int color) {
        drawRect(x, y, 1, 1, color);
    }

    public static void drawTextureModalRect(Gui gui, int x, int y, int u, int v, int width, int height) {
        gui.drawTexturedModalRect(x, y, u, v, width, height);
    }

    public static void drawTextureModalRectFromRight(Gui gui, int x, int y, int u, int v, int width, int height, int maxWidth) {
        int leftShift = maxWidth - width;
        gui.drawTexturedModalRect(x + leftShift, y, u + leftShift, v, width, height);
    }

    public enum EnumFillRotation {
        TOP_DOWN,
        LEFT_RIGHT
    }
}
