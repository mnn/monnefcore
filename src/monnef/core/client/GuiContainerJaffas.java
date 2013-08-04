/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import monnef.core.utils.ColorHelper;
import monnef.core.utils.GuiHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class GuiContainerJaffas extends GuiContainer {
    private static ArrayList<String> tooltips = new ArrayList<String>();

    public GuiContainerJaffas(Container container) {
        super(container);
    }

    public static void drawPlasticBox(GuiContainerJaffas gui, int x, int y, int width, int height) {
        gui.drawHorizontalLine(x, width - 2, y, GuiHelper.COLOR_DARK_GRAY);
        gui.drawVerticalLine(x, y, height - 1, GuiHelper.COLOR_DARK_GRAY);

        gui.drawHorizontalLine(x + 1, width - 2, y + height - 1, GuiHelper.COLOR_WHITE);
        gui.drawVerticalLine(x + width - 1, y, height - 1, GuiHelper.COLOR_WHITE);

        GuiHelper.drawPixel(x + width - 1, y, GuiHelper.COLOR_GRAY);
        GuiHelper.drawPixel(x, y + height - 1, GuiHelper.COLOR_GRAY);
    }

    @Override
    public void drawHorizontalLine(int x, int width, int y, int color) {
        super.drawHorizontalLine(x, x + width, y, color);
    }

    @Override
    protected void drawVerticalLine(int x, int y, int height, int color) {
        super.drawVerticalLine(x, y, y + height, color);
    }

    public void drawBottomUpBar(int x, int y, int value, int width, int height, ColorHelper.IntColor topColor, ColorHelper.IntColor bottomColor) {
        drawPlasticBox(this, x, y, width, height);
        GuiHelper.drawRect(x + 1, y + 1, width - 2, height - 2, GuiHelper.COLOR_GRAY);
        if (value != 0) {
            GuiHelper.drawGradientRectFromDown(this, x + 1, y + 1, width - 2, value, topColor, bottomColor, GuiHelper.EnumFillRotation.TOP_DOWN, height - 2);
        }
        GL11.glColor4f(1, 1, 1, 1);
    }

    @Override
    public void drawScreen(int mousex, int mousey, float par3) {
        super.drawScreen(mousex, mousey, par3);
        drawTooltips(mousex, mousey);
    }

    public List<String> fillTooltips(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
        return currenttip;
    }

    private void drawTooltips(int mousex, int mousey) {
        tooltips.clear();
        fillTooltips(this, mousex, mousey, tooltips);
        drawToolTip(tooltips, mousex, mousey);
    }

    // based on GuiContainer
    public void drawToolTip(List<String> tooltip, int x, int y) {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        if (!tooltip.isEmpty()) {
            int maximalWidth = 0;
            Iterator iterator = tooltip.iterator();

            while (iterator.hasNext()) {
                String currentText = (String) iterator.next();
                int stringWidth = this.fontRenderer.getStringWidth(currentText);

                if (stringWidth > maximalWidth) {
                    maximalWidth = stringWidth;
                }
            }

            int xcoord = x + 12;
            int ycoord = y - 12;
            int yoffset = 8;

            if (tooltip.size() > 1) {
                yoffset += 2 + (tooltip.size() - 1) * 10;
            }

            int color1 = -267386864;
            this.drawGradientRect(xcoord - 3, ycoord - 4, xcoord + maximalWidth + 3, ycoord - 3, color1, color1);
            this.drawGradientRect(xcoord - 3, ycoord + yoffset + 3, xcoord + maximalWidth + 3, ycoord + yoffset + 4, color1, color1);
            this.drawGradientRect(xcoord - 3, ycoord - 3, xcoord + maximalWidth + 3, ycoord + yoffset + 3, color1, color1);
            this.drawGradientRect(xcoord - 4, ycoord - 3, xcoord - 3, ycoord + yoffset + 3, color1, color1);
            this.drawGradientRect(xcoord + maximalWidth + 3, ycoord - 3, xcoord + maximalWidth + 4, ycoord + yoffset + 3, color1, color1);
            int color2 = 1347420415;
            int color3 = (color2 & 16711422) >> 1 | color2 & -16777216;
            this.drawGradientRect(xcoord - 3, ycoord - 3 + 1, xcoord - 3 + 1, ycoord + yoffset + 3 - 1, color2, color3);
            this.drawGradientRect(xcoord + maximalWidth + 2, ycoord - 3 + 1, xcoord + maximalWidth + 3, ycoord + yoffset + 3 - 1, color2, color3);
            this.drawGradientRect(xcoord - 3, ycoord - 3, xcoord + maximalWidth + 3, ycoord - 3 + 1, color2, color2);
            this.drawGradientRect(xcoord - 3, ycoord + yoffset + 2, xcoord + maximalWidth + 3, ycoord + yoffset + 3, color3, color3);

            for (int i = 0; i < tooltip.size(); ++i) {
                String currTooltip = tooltip.get(i);

                this.fontRenderer.drawStringWithShadow(currTooltip, xcoord, ycoord, -1);

                if (i == 0) {
                    ycoord += 2;
                }

                ycoord += 10;
            }
        }
    }
}
