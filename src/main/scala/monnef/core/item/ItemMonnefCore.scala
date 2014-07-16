package monnef.core.item

import monnef.core.MonnefCorePlugin
import monnef.core.common.CustomIconHelper
import net.minecraft.client.renderer.texture.{IIconRegister}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumRarity, Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{MathHelper, IIcon}
import monnef.core.block.GameObjectDescriptor
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.creativetab.CreativeTabs
import monnef.core.mod.MonnefCoreNormalMod
import cpw.mods.fml.common.registry.LanguageRegistry
import java.util
import monnef.core.utils.scalautils._

abstract class ItemMonnefCore extends Item with GameObjectDescriptor {

  import ItemMonnefCore._

  private var inBetaStage: Boolean = _
  private var info: String = null
  protected var rarity: Int = 0
  private var secondCreativeTab: CreativeTabs = null

  initCustomIcon()
  rarity = MonnefCoreNormalMod.proxy.getCommonRarity
  if (this.isInstanceOf[MultiItem]) this.asInstanceOf[MultiItem].initMulti()

  // this.tryAsInstanceOf((a: MultiItem) => a.initMulti()) // <- not working, maybe too much is erased?

  override def registerIcons(iconRegister: IIconRegister) {
    this.itemIcon = iconRegister.registerIcon(CustomIconHelper.generateId(this))
    if (iconsCount > 1) {
      icons = new Array[IIcon](iconsCount)
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

  // raw type stuff
  final override def addInformation(stack: ItemStack, player: EntityPlayer, result: java.util.List[_], par4: Boolean): Unit = {
    addInformationCustom(stack, player, result.asInstanceOf[util.List[String]], par4)
  }

  def addInformationCustom(stack: ItemStack, player: EntityPlayer, result: java.util.List[String], par4: Boolean): Unit = {
    super.addInformation(stack, player, result, par4)
    if (inBetaStage) {
      result.add(BETA_WARNING_TEXT)
    }
    if (info != null) result.add(info)
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

  override def setHasSubtypes(hasSubtypes: Boolean): Item = super.setHasSubtypes(hasSubtypes)
}

object ItemMonnefCore {
  def initNBT(stack: ItemStack) {
    if (stack.getTagCompound == null)
      stack.setTagCompound(new NBTTagCompound())
  }

  val BETA_WARNING_TEXT: String = "\u00A7l" + "not finished!\u00A7r"
}

// to overcome Scala's imperfections
trait MultiItemBase {
  def getUnlocalizedName: String
}

trait MultiItem extends MultiItemBase {
  this: ItemMonnefCore =>

  def initMulti() {
    setMaxDamage(0)
    setHasSubtypes(true)
    setIconsCount(getSubItemsCount)
  }

  def getSubNames: Array[String]

  def getSubTitles: Array[String]

  def getSubItemsCount: Int = getSubNames.length

  @SideOnly(Side.CLIENT) override def getIconFromDamage(iconId: Int): IIcon = {
    val iconNum = MathHelper.clamp_int(iconId, 0, getSubItemsCount)
    getCustomIcon(iconNum)
  }

  abstract override def getUnlocalizedName: String = super.getUnlocalizedName

  override def getUnlocalizedName(stack: ItemStack): String = {
    val idx = MathHelper.clamp_int(stack.getItemDamage, 0, getSubItemsCount)
    super.getUnlocalizedName + "." + getSubNames(idx)
  }

  def registerNames() {
    for (i <- 0 until getSubItemsCount)
      LanguageRegistry.instance.addStringLocalization(this.getUnlocalizedName + "." + getSubNames(i) + ".name", getSubTitles(i))
  }

  // raw type issue when java -> scala -> java/scala class hierarchy, use getSubItemsCustom
  @SideOnly(Side.CLIENT) final override def getSubItems(item: Item, tabs: CreativeTabs, result: util.List[_]) {
    getSubItemsCustom(item, tabs, result.asInstanceOf[util.List[ItemStack]])
  }

  @SideOnly(Side.CLIENT)
  def getSubItemsCustom(item: Item, tabs: CreativeTabs, result: util.List[ItemStack]) {
    for (i <- 0 until getSubItemsCount) result.add(new ItemStack(item, 1, i))
  }
}