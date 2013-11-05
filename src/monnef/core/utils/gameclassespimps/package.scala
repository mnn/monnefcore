/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

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

}
