/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;

import static monnef.core.utils.ColorHelper.IntColor;

public class DrawingHelper {
    public static void drawRect(int x, int y, int width, int height, IntColor color) {
        Gui.drawRect(x, y, x + width, y + height, color.toInt());
    }

    public static void setTessellatorColor(Tessellator tessellator, IntColor color) {
        tessellator.setColorRGBA_F(color.getFloatRed(), color.getFloatGreen(), color.getFloatBlue(), color.getFloatAlpha());
    }
}
