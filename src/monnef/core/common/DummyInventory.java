/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

class DummyInventory implements IInventory {

    private ItemStack stack;

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return stack;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return stack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        stack = itemstack;
    }

    @Override
    public String getInvName() {
        return "DummyInv";
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void onInventoryChanged() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }
}
