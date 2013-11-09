/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayer

package object gameclassespimps {

  implicit class ItemStackPimps(s: ItemStack) {
    def initTag() { if (s.getTagCompound == null) s.setTagCompound(new NBTTagCompound()) }

    /**
     * Grabs NBT, initializes it if necessary.
     * @return NBT
     */
    def getValidTagCompound(): NBTTagCompound = {
      initTag()
      s.getTagCompound
    }
  }

  implicit class EntityPlayerPimps(p: EntityPlayer) {
    def hotBarStacks: Seq[ItemStack] = for (i <- 0 to 8) yield p.inventory.getStackInSlot(i)

    def invStacksWithoutHotBar: Seq[ItemStack] = for (i <- 9 until p.inventory.getSizeInventory) yield p.inventory.getStackInSlot(i)

    def allInvAndArmorStacks: Seq[ItemStack] = p.inventory.mainInventory ++ armorStacks

    def armorStacks: Seq[ItemStack] = p.inventory.armorInventory
  }

}
