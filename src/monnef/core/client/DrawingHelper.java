/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import static monnef.core.utils.ColorHelper.IntColor;

public class DrawingHelper {
    public static void drawRect(int x, int y, int width, int height, IntColor color) {
        Gui.drawRect(x, y, x + width, y + height, color.toInt());
    }

    public static void setTessellatorColor(Tessellator tessellator, IntColor color) {
        tessellator.setColorRGBA_F(color.getFloatRed(), color.getFloatGreen(), color.getFloatBlue(), color.getFloatAlpha());
    }

    public static void drawGradientRect(Gui gui, int x, int y, int width, int height, IntColor topColor, IntColor bottomColor, EnumFillRotation rotation) {
        drawGradientRectInternal(x, y, x + width, y + height, topColor, bottomColor, rotation, gui.zLevel);
    }

    public static void drawGradientRectFromDown(Gui gui, int x, int y, int width, int height, IntColor topColor, IntColor bottomColor, EnumFillRotation rotation, int maxHeight) {
        int r = maxHeight - height;
        drawGradientRectInternal(x, y + r, x + width, y + maxHeight, topColor, bottomColor, rotation, gui.zLevel);
    }

    // heavily based on Gui.drawGradientRect
    private static void drawGradientRectInternal(int x, int y, int x2, int y2, IntColor colorTop, IntColor colorBottom, EnumFillRotation rotation, double zLevel) {
        if (rotation == EnumFillRotation.LEFT_RIGHT) {
            IntColor tmp = colorTop;
            colorTop = colorBottom;
            colorBottom = tmp;
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        setTessellatorColor(tessellator, colorTop);
        tessellator.addVertex((double) x2, (double) y, zLevel);
        if (rotation == EnumFillRotation.LEFT_RIGHT)
            setTessellatorColor(tessellator, colorBottom);
        tessellator.addVertex((double) x, (double) y, zLevel);
        setTessellatorColor(tessellator, colorBottom);
        tessellator.addVertex((double) x, (double) y2, zLevel);
        if (rotation == EnumFillRotation.LEFT_RIGHT)
            setTessellatorColor(tessellator, colorTop);
        tessellator.addVertex((double) x2, (double) y2, zLevel);
        tessellator.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public enum EnumFillRotation {
        TOP_DOWN,
        LEFT_RIGHT
    }
}
