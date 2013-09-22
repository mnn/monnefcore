/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import monnef.core.utils.ColorEnum;
import monnef.core.utils.GuiHelper;
import net.minecraft.inventory.Container;

public class GuiExporter extends GuiContainerJaffas {
    public GuiExporter(Container container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GuiHelper.drawRect(x, y, xSize, ySize, ColorEnum.MAGENTA.getInt());
    }
}
