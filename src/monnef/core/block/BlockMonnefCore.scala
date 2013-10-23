package monnef.core.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import monnef.core.MonnefCorePlugin
import monnef.core.common.CustomIconHelper
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.util.Icon


abstract class BlockMonnefCore(_id: Int, _material: Material) extends Block(_id, _material) with CustomIcon {
  if (!classOf[CustomIconDescriptor].isAssignableFrom(this.getClass())) {
    throw new RuntimeException(this.getClass().getSimpleName() + " does not implement CustomIconDescriptor.")
  } else {
    this.asInstanceOf[CustomIconDescriptor].setupDefaultValuesFromBlockDescriptor(this)
  }

  def this(id: Int, index: Int, material: Material) = {
    this(id, material)
    this.customIconIndex = index
  }

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IconRegister): Unit = {
    this.blockIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this))
    if (iconsCount > 1) {
      icons = new Array[Icon](iconsCount)
      icons(0) = this.blockIcon
      var i: Int = 1
      while (i < iconsCount) {
        icons(i) = iconRegister.registerIcon(CustomIconHelper.generateShiftedId(this, i))
        i = i + 1
      }
    }
  }

  def removeFromCreativeTab(): Unit = {
    if (!MonnefCorePlugin.debugEnv)
      setCreativeTab(null)
  }
}
