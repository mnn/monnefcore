/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class SlotLocked extends SlotOutput {

    public SlotLocked(IInventory inventory, int slotNumber, int x, int y) {
        super(inventory, slotNumber, x, y);
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return false;
    }
}
