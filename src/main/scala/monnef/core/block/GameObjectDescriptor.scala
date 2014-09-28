/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.block

import monnef.core.api.ICustomIcon
import net.minecraft.util.IIcon

trait GameObjectDescriptor extends ICustomIcon {
  protected var customIconIndex: Int = _
  protected var sheetNumber: Int = _
  protected var iconsCount: Int = 1
  protected var icons: Array[IIcon] = null
  protected var modName: String = null

  override def setModName(newModName: String): Unit = { modName = newModName }

  override def setCustomIconIndex(index: Int): Unit = { this.customIconIndex = index }

  override def getSheetNumber: Int = sheetNumber

  override def getCustomIconIndex: Int = customIconIndex

  override def setSheetNumber(index: Int): Unit = { this.sheetNumber = index }

  override def getCustomIconName: String = null

  override def getIconsCount: Int = iconsCount

  override def setIconsCount(iconsCount: Int): Unit = { this.iconsCount = iconsCount }

  def initCustomIcon() {
    if (!classOf[CustomIconDescriptor].isAssignableFrom(this.getClass)) {
      throw new RuntimeException(this.getClass.getSimpleName + " does not implement CustomIconDescriptor.")
    } else {
      this.asInstanceOf[CustomIconDescriptor].setupDefaultValuesFromBlockDescriptor(this)
    }
  }

  override final def getModName: String = {
    if (modName == null) {
      throw new RuntimeException(String.format("Item/block %s was not properly initialized, it is missing mod mapping.", this.getClass.getSimpleName))
    }
    modName
  }

  override def getCustomIcon(index: Int): IIcon = icons(index)
}
