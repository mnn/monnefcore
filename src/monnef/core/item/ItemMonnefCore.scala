package monnef.core.item

import monnef.core.MonnefCorePlugin
import monnef.core.common.CustomIconHelper
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Icon
import monnef.core.block.CustomIcon

abstract class ItemMonnefCore(_id: Int) extends Item(_id) with CustomIcon {

  import ItemMonnefCore._

  private var inBetaStage: Boolean = _

  initCustomIcon()

  override def registerIcons(iconRegister: IconRegister) {
    this.itemIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this))
    if (iconsCount > 1) {
      icons = new Array[Icon](iconsCount)
      icons(0) = this.itemIcon

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

  def markAsBeta(): Unit = {
    inBetaStage = true
  }

  // TODO: weird override issue...
  override def addInformation(stack: ItemStack, player: EntityPlayer, result: java.util.List[_], par4: Boolean): Unit = {
    //super.addInformation(stack, player, result.asInstanceOf[java.util.List[_]], par4)
    super.addInformation(stack, player, result, par4) // from this compiler said that List[_] is expected
    if (inBetaStage) {
      result.asInstanceOf[java.util.List[String]].add(BETA_WARNING_TEXT)
    }
  }
}

object ItemMonnefCore {
  def initNBT(stack: ItemStack) {
    if (stack.getTagCompound == null)
      stack.setTagCompound(new NBTTagCompound())
  }

  val BETA_WARNING_TEXT: String = "\u00A7lnot finished!\u00A7r"
}
