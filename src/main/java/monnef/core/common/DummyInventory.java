/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class DummyInventory implements IInventory {

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
    public String getInventoryName() {
        return "DummyInv";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }
}
