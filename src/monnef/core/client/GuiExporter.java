/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.ColorEnum;
import monnef.core.utils.GuiHelper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class GuiExporter extends GuiContainerJaffas {
    public static final int BUTTON_TAKE_SHOT = 0;
    public static final int BUTTON_TAKE_SHOTS = 1;
    public static final int BUTTON_PROCESS_ALL = 2;

    public GuiExporter(Container container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GuiHelper.drawRect(x, y, xSize, ySize, ColorEnum.MAGENTA.getInt());
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(BUTTON_TAKE_SHOT, x + 10, y + 32, 100, 20, "Take shot"));
        buttonList.add(new GuiButton(BUTTON_TAKE_SHOTS, x + 10, y + 52, 100, 20, "Take shots"));
        buttonList.add(new GuiButton(BUTTON_PROCESS_ALL, x + 10, y + 72, 100, 20, "Process all"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case BUTTON_TAKE_SHOT:
                ExporterTickHandler.scheduleTask(new ItemStack(Block.chest), -1);
                break;

            case BUTTON_TAKE_SHOTS:
                ExporterTickHandler.scheduleTask(new ItemStack(Block.blockLapis), -1);
                ExporterTickHandler.scheduleTask(new ItemStack(Block.hopperBlock), -1);
                ExporterTickHandler.scheduleTask(new ItemStack(Block.dispenser), -1);
                break;

            case BUTTON_PROCESS_ALL:
                int c = 0;
                for (int i = 1; i < Block.blocksList.length; i++) {
                    Block b = Block.blocksList[i];
                    if (b != null) {
                        ArrayList<ItemStack> o = new ArrayList<ItemStack>();
                        b.getSubBlocks(i, null, o);
                        for (int j = 0; j < o.size(); j++) {
                            ItemStack stack = o.get(j);
                            if (stack != null) {
                                ExporterTickHandler.scheduleTask(stack, i);
                                c++;
                            } else {
                                MonnefCorePlugin.Log.printWarning("Possible badly coded block - id: " + i + ", name: " + b.getUnlocalizedName());
                            }
                        }
                    }
                }
                FMLClientHandler.instance().getClient().thePlayer.addChatMessage("Queued " + c + " blocks to process.");
                break;
        }
    }
}
