package monnef.core.block

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import monnef.core.MonnefCorePlugin
import monnef.core.common.CustomIconHelper
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import javax.swing.Icon
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon


abstract class BlockMonnefCore(_material: Material) extends Block(_material) with GameObjectDescriptor {
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
}
