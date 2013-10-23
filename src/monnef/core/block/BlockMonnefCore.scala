package monnef.core.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import monnef.core.MonnefCorePlugin
import monnef.core.common.CustomIconHelper
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.util.Icon


abstract class BlockMonnefCore(_id: Int, _material: Material) extends Block(_id, _material) with GameObjectDescriptor {
  initCustomIcon()

  def this(id: Int, index: Int, material: Material) = {
    this(id, material)
    this.customIconIndex = index
  }

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IconRegister) {
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

  def removeFromCreativeTab() {
    if (!MonnefCorePlugin.debugEnv)
      setCreativeTab(null)
  }
}
