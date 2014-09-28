/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class AdvancedButton extends GuiButton {
    public AdvancedButton(int id, int x, int y, String text) {
        super(id, x, y, text);
    }

    public AdvancedButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        return super.mousePressed(par1Minecraft, par2, par3);
    }

    public boolean mousePressedAdvanced(Minecraft mc, int x, int y, int button) {
        return this.enabled && this.visible && doesReactToThisButton(button) && x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
    }

    public boolean doesReactToThisButton(int button) {
        return button == 0 || button == 1;
    }
}
