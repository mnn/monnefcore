/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils

import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.Entity
import net.minecraft.world.World
import scala.collection.JavaConversions._
import net.minecraft.block.{BlockLiquid, Block}
import scalautils._
import net.minecraftforge.fluids.{BlockFluidBase, BlockFluidFinite}
import cpw.mods.fml.relauncher.ReflectionHelper

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

  implicit class ItemPimps(i: Item) {
    def getBlock: Option[Block] = {
      ItemHelper.getBlockFromItemViaGameData(i).toOption
    }
  }

  implicit class BlockFluidBasePimps(b: BlockFluidBase) {
    private[this] lazy val quantaPerBlockField = ReflectionHelper.findField(classOf[BlockFluidBase], "quantaPerBlock")

    def getQuantaPerBlock = quantaPerBlockField.getInt(b)

    def getMaxMeta(): Int = b.getMaxRenderHeightMeta

    def isFullyFilled(world: World, x: Int, y: Int, z: Int): Boolean = b.getQuantaValue(world, x, y, z) == b.getQuantaPerBlock
  }

  implicit class BlockFluidFinitePimps(b: BlockFluidFinite) {
  }

  implicit class BlockPimps(b: Block) {
    def isFluid: Boolean = b match {
      case _: BlockLiquid => true
      case _: BlockFluidBase => true
      case _ => false
    }

    def isUnbreakable(w: World, x: Int, y: Int, z: Int): Boolean = b.getBlockHardness(w, x, y, z) < 0
  }

}
