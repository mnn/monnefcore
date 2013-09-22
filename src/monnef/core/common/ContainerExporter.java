/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common;

import monnef.core.client.SlotLocked;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerExporter extends Container {

    private DummyInventory inv;

    public ContainerExporter() {
        inv = new DummyInventory();
        inv.setInventorySlotContents(0, new ItemStack(Block.grass));
        addSlotToContainer(new SlotLocked(inv, 0, 0, 0));
    }

    public DummyInventory getInv() {
        return inv;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }
}
