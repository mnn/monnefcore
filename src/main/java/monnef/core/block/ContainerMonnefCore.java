package monnef.core.block;

import monnef.core.MonnefCorePlugin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ContainerMonnefCore extends Container {
    private static final boolean PRINT_DEBUG_TRANSFER_MESSAGES = false;

    protected InventoryPlayer playerInventory;
    protected IInventory inventory;

    public ContainerMonnefCore(InventoryPlayer inventoryPlayer, IInventory inventory) {
        this.playerInventory = inventoryPlayer;
        this.inventory = inventory;
        constructSlotsFromInventoryAndBindPlayerInventory(this.inventory);
    }

    public int getYSize() {
        return 166;
    }

    public int getYPlayerInvShift() {
        return 0;
    }

    public int getXSize() {
        return 176;
    }

    public int getXPlayerInvShift() {
        return 0;
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        int yShift = getYSize() - 82 + getYPlayerInvShift(); // 84
        int xShift = 8 + getXPlayerInvShift(); // 8
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                        xShift + j * 18, yShift + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, xShift + i * 18, 142 - 84 + yShift));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        int slots = getSlotsCount();

        if (PRINT_DEBUG_TRANSFER_MESSAGES && MonnefCorePlugin.debugEnv) {
            MonnefCorePlugin.Log.printDebug(this.getClass().getSimpleName() + ": transferStackInSlot - slot#=" + slot);
        }

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //merges the item into player inventory since its in the tileEntity
            if (slot < slots) {
                if (!this.mergeItemStack(stackInSlot, slots, 36 + slots, true)) {
                    return null;
                }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else {
                if (!this.mergeItemStack(stackInSlot, 0, getInputSlotsCount(), false)) {
                    return null;
                }
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize) {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return stack;
    }

    public abstract int getSlotsCount();

    public abstract int getOutputSlotsCount();

    public int getInputSlotsCount() {
        return getSlotsCount() - getOutputSlotsCount();
    }

    public int getStartIndexOfOutput() {
        return getSlotsCount() - getOutputSlotsCount();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }


    public void constructSlotsFromInventoryAndBindPlayerInventory(IInventory inv) {
        constructSlotsFromInventory(inv);
        bindPlayerInventory(playerInventory);
    }

    /**
     * Creates slots via addSlotToContainer.
     * Output slots *must* be created last!
     *
     * @param inv The inventory.
     */
    public abstract void constructSlotsFromInventory(IInventory inv);

    /**
     * Merges provided ItemStack with the first available one in the (container/player) inventory
     *
     * @param stack         Item to move/insert/merge.
     * @param startingIndex Starting index in inventory.
     * @param endingIndex   Last index in inventory.
     * @param fromEnd       Start searching from end of an inventory.
     * @return True on partial merge, False on full merge (nothing in {@code stack} is left).
     */
    @Override
    public boolean mergeItemStack(ItemStack stack, int startingIndex, int endingIndex, boolean fromEnd) {
        return super.mergeItemStack(stack, startingIndex, endingIndex, fromEnd);
    }

    public List getCrafters() {
        return crafters;
    }
}
