/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import monnef.core.utils.ColorHelper;
import monnef.core.utils.GuiHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainerJaffas extends GuiContainer {
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
        GuiHelper.drawGradientRectFromDown(this, x + 1, y + 1, width - 2, value, topColor, bottomColor, GuiHelper.EnumFillRotation.TOP_DOWN, height - 2);
        GL11.glColor4f(1, 1, 1, 1);
    }
}
