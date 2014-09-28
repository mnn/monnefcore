/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.PlayerHelper;
import monnef.core.utils.ScreenShotHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.util.LinkedList;

public class ExporterTickHandler {
    private static boolean takeShotNow = false;

    private static LinkedList<RenderTask> tasks = new LinkedList<RenderTask>();
    private RenderTask currentTask;

    public static void scheduleTask(ItemStack stack, Item origItem) {
        tasks.add(new RenderTask(stack, origItem));
    }

    public void onTickEvent(TickEvent evt) {
    }

    @SubscribeEvent
    public void tickStart(TickEvent.ClientTickEvent evt) {
        if (evt.phase == TickEvent.Phase.START) {
            if (takeShotNow) {
                takeShot();
                takeShotNow = false;
            } else if (!tasks.isEmpty() && !takeShotNow) {
                GuiExporter gui = (GuiExporter) FMLClientHandler.instance().getClient().currentScreen;
                currentTask = tasks.pop();
                if (currentTask == null) {
                    MonnefCorePlugin.Log.printWarning("Null in tasks list!");
                } else if (currentTask.stack == null) {
                    MonnefCorePlugin.Log.printWarning("Null in task's stack!");
                } else {
                    try {
                        MonnefCorePlugin.Log.printFinest("Adding to render queue block: " + currentTask.stack.getUnlocalizedName());
                        gui.inventorySlots.getSlot(0).inventory.setInventorySlotContents(0, currentTask.stack);
                        takeShotNow = true;
                    } catch (NullPointerException e) {
                        MonnefCorePlugin.Log.printWarning("NPE while popping task! BlockID: " + currentTask.stack.getItem().getUnlocalizedName() + " (origId: " + currentTask.origItem + ")");
                        takeShotNow = false;
                    }
                }
            }
        }
    }

    /*
    @SubscribeEvent
    public void tickEnd(TickEvent.ClientTickEvent evt) {
        if (evt.phase == TickEvent.Phase.START) {
            if (!takeShotNow) return;

        }
    }
    */

    private void takeShot() {
        GuiExporter gui = (GuiExporter) FMLClientHandler.instance().getClient().currentScreen;
        Minecraft mc = FMLClientHandler.instance().getClient();
        int pixX = gui.x * mc.displayWidth / gui.width;
        int pixY = (gui.y - gui.height /*+ 1*/) * (-mc.displayHeight) / gui.height;
        int iconSize = 32;
        String blockName = currentTask.stack.getUnlocalizedName();
        String fileName = blockName + ".png";
        File currDir = new File(".");
        if (new File(currDir, fileName).exists()) {
            MonnefCorePlugin.Log.printWarning("Overwriting '" + fileName + "'!");
        }
        String res = ScreenShotHelper.saveScreenShot(currDir, fileName, pixX, pixY - iconSize, iconSize, iconSize);
        if (res.startsWith("Saved")) {
            MonnefCorePlugin.Log.printFinest("Rendered block " + blockName + " (oItem:" + currentTask.origItem.getUnlocalizedName() + ", sItem:" + currentTask.stack.getItem().getUnlocalizedName() + ").");
        } else {
            PlayerHelper.addMessage(FMLClientHandler.instance().getClient().thePlayer, "Problem! " + res);
        }
    }

    public static class RenderTask {
        public final ItemStack stack;
        public final Item origItem;

        public RenderTask(ItemStack stack, Item origItem) {
            this.stack = stack;
            this.origItem = origItem;
        }
    }
}
