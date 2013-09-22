/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common;

import cpw.mods.fml.common.network.IGuiHandler;
import monnef.core.client.GuiExporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public enum GuiId {
        EXPORTER
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID < 0 || ID > GuiId.values().length) return null;
        GuiId gui = GuiId.values()[ID];
        switch (gui) {
            case EXPORTER:
                return new ContainerExporter();

            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID < 0 || ID > GuiId.values().length) return null;
        GuiId gui = GuiId.values()[ID];
        switch (gui) {
            case EXPORTER:
                return new GuiExporter(new ContainerExporter());

            default:
                return null;
        }
    }
}
