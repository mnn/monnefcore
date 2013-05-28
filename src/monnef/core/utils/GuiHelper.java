/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.client.gui.GuiScreen;

public class GuiHelper {
    public static void drawModalRectFromDown(GuiScreen gui, int x, int y, int textureX, int textureY, int width, int height, int heightMax) {
        // x, y, u, v, width, height
        int invHeight = heightMax - height;
        gui.drawTexturedModalRect(x, y + invHeight, textureX, textureY + invHeight, width, height);
    }
}
