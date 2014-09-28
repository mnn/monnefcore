/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.block;

import monnef.core.client.SlotLocked;
import monnef.core.common.DummyInventory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerExporter extends ContainerMonnefCore {

    public ContainerExporter(InventoryPlayer playerInventory) {
        super(playerInventory, new DummyInventory());
        inventory.setInventorySlotContents(0, new ItemStack(Blocks.grass));
    }

    @Override
    public int getSlotsCount() {
        return 1;
    }

    @Override
    public int getOutputSlotsCount() {
        return 0;
    }

    @Override
    public void constructSlotsFromInventory(IInventory inv) {
        addSlotToContainer(new SlotLocked(inv, 0, 0, 0));
    }
}
