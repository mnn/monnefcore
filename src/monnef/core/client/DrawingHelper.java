/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.client.renderer.Tessellator;

import static monnef.core.utils.ColorHelper.IntColor;

public class DrawingHelper {
    public static void drawRect(double x, double y, double width, double heigh, IntColor color) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        setTessellatorColor(tessellator, color);

        double x1 = x;
        double x2 = x + width;
        double y1 = y;
        double y2 = y + heigh;
        tessellator.addVertex(x1, y1, 0.0D);
        tessellator.addVertex(x1, y2, 0.0D);
        tessellator.addVertex(x2, y2, 0.0D);
        tessellator.addVertex(x2, y1, 0.0D);
        tessellator.draw();
    }

    public static void setTessellatorColor(Tessellator tessellator, IntColor color) {
        tessellator.setColorRGBA_F(color.getFloatRed(), color.getFloatGreen(), color.getFloatBlue(), color.getFloatAlpha());
    }
}
