package monnef.core.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import monnef.core.MonnefCorePlugin
import monnef.core.common.CustomIconHelper
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import javax.swing.Icon
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.{MovingObjectPosition, IIcon}
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.item.{ItemBlock, Item, ItemStack}
import monnef.core.api.IItemBlock


abstract class BlockMonnefCore(_material: Material) extends Block(_material) with GameObjectDescriptor {

  import BlockMonnefCore._

  initCustomIcon()

  def this(index: Int, material: Material) = {
    this(material)
    this.customIconIndex = index
  }

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(iconRegister: IIconRegister) {
    this.blockIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this))
    if (iconsCount > 1) {
      icons = new Array[IIcon](iconsCount)
      icons(0) = this.blockIcon
      var i: Int = 1
      while (i < iconsCount) {
        icons(i) = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, i))
        i = i + 1
      }
    }
  }

  def removeFromCreativeTab() {
    if (!MonnefCorePlugin.debugEnv)
      setCreativeTab(null)
  }

  def setBurnProperties(encouragement: Int, flammibility: Int) {
    queueSetBurnProperties(this, encouragement, flammibility)
  }

  override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int): ItemStack = {
    val item = getItem(world, x, y, z)
    if (item == null) null
    else {
      if (item.isInstanceOf[IItemBlock]) new ItemStack(item, 1, Block.getBlockFromItem(item).getDamageValue(world, x, y, z))
      else super.getPickBlock(target, world, x, y, z)
    }
  }
}

object BlockMonnefCore {
  private var initialized: Boolean = false

  private case class BurnPropertiesRecord(block: Block, encouragement: Int, flammibility: Int)

  private var burnPropertiesQueue: Seq[BurnPropertiesRecord] = Seq()

  def onPostLoad() {
    if (initialized) throw new RuntimeException("re-initialization!")
    initialized = true

    burnPropertiesQueue.foreach { case BurnPropertiesRecord(block, encouragement, flammibility) => setBurnProperties(block, encouragement, flammibility)}
    burnPropertiesQueue = null
  }

  def queueSetBurnProperties(block: Block, encouragement: Int, flammibility: Int) {
    burnPropertiesQueue :+= BurnPropertiesRecord(block, encouragement, flammibility)
  }

  private def setBurnProperties(block: Block, encouragement: Int, flammibility: Int) {
    Blocks.fire.setFireInfo(block, encouragement, flammibility)
  }
}
