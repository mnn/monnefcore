package monnef.core.item

import monnef.core.MonnefCorePlugin
import monnef.core.common.CustomIconHelper
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumRarity, Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Icon
import monnef.core.block.GameObjectDescriptor
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.creativetab.CreativeTabs
import monnef.core.mod.MonnefCoreNormalMod

abstract class ItemMonnefCore(_id: Int) extends Item(_id) with GameObjectDescriptor {

  import ItemMonnefCore._

  private var inBetaStage: Boolean = _
  private var info: String = null
  protected var rarity: Int = 0
  private var secondCreativeTab: CreativeTabs = null

  initCustomIcon()
  rarity = MonnefCoreNormalMod.proxy.getCommonRarity

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

  override def addInformation(stack: ItemStack, player: EntityPlayer, result: java.util.List[_], par4: Boolean): Unit = {
    super.addInformation(stack, player, result, par4)
    val r = result.asInstanceOf[java.util.List[String]]
    if (inBetaStage) {
      r.add(BETA_WARNING_TEXT)
    }
    if (info != null) r.add(info)
  }

  def setInfo(text: String) {
    this.info = text
  }

  def setSecondCreativeTab(tab: CreativeTabs) {
    this.secondCreativeTab = tab
  }

  override def getCreativeTabs: Array[CreativeTabs] =
    if (secondCreativeTab != null) Array[CreativeTabs](secondCreativeTab, getCreativeTab)
    else super.getCreativeTabs

  def setRarity(rarity: Int) { this.rarity = rarity }

  @SideOnly(Side.CLIENT) override def getRarity(stack: ItemStack): EnumRarity = EnumRarity.values()(rarity)

  def getCustomDamageVsEntity: Int = 1

  def getCustomDamageVsEntity(itemStack: ItemStack): Int = getCustomDamageVsEntity

}

object ItemMonnefCore {
  def initNBT(stack: ItemStack) {
    if (stack.getTagCompound == null)
      stack.setTagCompound(new NBTTagCompound())
  }

  val BETA_WARNING_TEXT: String = "\u00A7lnot finished!\u00A7r"
}
