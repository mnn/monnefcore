/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import monnef.core.MonnefCorePlugin;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class ContainerMachine extends TileContainerMonnefCore {
    protected TileMachineWithInventory machine;

    protected ArrayList<Integer> lastValue;

    @Override
    public void constructSlotsFromTile(TileEntity inv) {
        addSlotToContainer(new Slot((IInventory) inv, 0, 80, 25));
    }

    public ContainerMachine(InventoryPlayer inventoryPlayer, TileMachineWithInventory te) {
        super(inventoryPlayer, te);
        machine = te;

        lastValue = new ArrayList<Integer>();
        for (int i = 0; i < machine.getIntegersToSyncCount(); i++) {
            lastValue.add(0);
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        for (int i = 0; i < lastValue.size(); i++) {
            crafting.sendProgressBarUpdate(this, i, this.machine.getCurrentValueOfIntegerToSync(i));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int craftersCounter = 0; craftersCounter < this.crafters.size(); ++craftersCounter) {
            ICrafting crafter = (ICrafting) this.crafters.get(craftersCounter);

            for (int i = 0; i < lastValue.size(); i++) {
                if (lastValue.get(i) != machine.getCurrentValueOfIntegerToSync(i)) {
                    crafter.sendProgressBarUpdate(this, i, machine.getCurrentValueOfIntegerToSync(i));
                }
            }
        }

        for (int i = 0; i < lastValue.size(); i++) {
            lastValue.set(i, machine.getCurrentValueOfIntegerToSync(i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        if (index < 0 || index > lastValue.size()) {
            if (MonnefCorePlugin.debugEnv) {
                System.err.println("skipping invalid index [" + index + "] of int to sync in container [" + this.getClass().getName() + "]");
            }
            return;
        }

        machine.setCurrentValueOfIntegerToSync(index, value);
    }
}
