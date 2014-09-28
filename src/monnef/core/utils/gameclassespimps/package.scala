/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.Entity
import net.minecraft.world.World
import scala.collection.JavaConversions._

package object scalagameutils {

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

  class EntityTypeSelector(c: Class[_]) extends IEntitySelector {
    def isEntityApplicable(entity: Entity): Boolean = entity != null && c.isAssignableFrom(entity.getClass)
  }

  implicit class WorldPimps(w: World) {
    def findEntitiesInRangeOfType[A](x: Int, y: Int, z: Int, radius: Int, c: Class[A]): List[A] =
      w.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius), new EntityTypeSelector(c)).asInstanceOf[java.util.List[A]].toList
  }

}
