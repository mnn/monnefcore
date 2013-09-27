/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import monnef.core.MonnefCorePlugin;
import monnef.core.utils.ScreenShotHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.util.EnumSet;
import java.util.LinkedList;

public class ExporterTickHandler implements ITickHandler {
    private static boolean takeShotNow = false;

    private static LinkedList<RenderTask> tasks = new LinkedList<RenderTask>();
    private RenderTask currentTask;

    public static void scheduleTask(ItemStack stack, int origId) {
        tasks.add(new RenderTask(stack, origId));
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        if (type.contains(TickType.CLIENT)) {
            if (!tasks.isEmpty() && !takeShotNow) {
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
                        MonnefCorePlugin.Log.printWarning("NPE while popping task! BlockID: " + currentTask.stack.itemID + " (origId: " + currentTask.origId + ")");
                        takeShotNow = false;
                    }
                }
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (!type.contains(TickType.RENDER)) return;
        if (!takeShotNow) return;
        takeShot();
        takeShotNow = false;
    }

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
            MonnefCorePlugin.Log.printFinest("Rendered block " + blockName + " (oID:" + currentTask.origId + ", sID:" + currentTask.stack.itemID + ").");
        } else {
            FMLClientHandler.instance().getClient().thePlayer.addChatMessage("Problem! " + res);
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER, TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "monnef-core-exporter";
    }

    public static class RenderTask {
        public final ItemStack stack;
        public final int origId;

        public RenderTask(ItemStack stack, int origId) {
            this.stack = stack;
            this.origId = origId;
        }
    }
}
