/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiHelper {
    public static void drawModalRectFromDown(GuiScreen gui, int x, int y, int textureX, int textureY, int width, int height, int heightMax) {
        // x, y, u, v, width, height
        int invHeight = heightMax - height;
        gui.drawTexturedModalRect(x, y + invHeight, textureX, textureY + invHeight, width, height);
    }

    public static boolean isMouseInRect(GuiContainer gui, int mouseX, int mouseY, int guiX, int guiY, int guiWidth, int guiHeight) {
        int x = (gui.width - gui.xSize) / 2;
        int y = (gui.height - gui.ySize) / 2;
        int mouseXinGui = mouseX - x;
        int mouseYinGui = mouseY - y;
        return mouseXinGui >= guiX && mouseXinGui < guiX + guiWidth &&
                mouseYinGui >= guiY && mouseYinGui < guiY + guiHeight;
    }
}
