/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.block;

import monnef.core.client.SlotLocked;
import monnef.core.common.DummyInventory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerExporter extends Container {

    private DummyInventory inv;

    public ContainerExporter() {
        inv = new DummyInventory();
        inv.setInventorySlotContents(0, new ItemStack(Blocks.grass));
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
