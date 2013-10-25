/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.block

import monnef.core.api.ICustomIcon

trait CustomIconDescriptor {
  def getDescriptorModName: String

  def getDescriptorSheetNumber: Int

  def setupDefaultValuesFromBlockDescriptor(obj: ICustomIcon) {
    obj.setModName(getDescriptorModName)
    obj.setSheetNumber(getDescriptorSheetNumber)
  }

  @Deprecated
  def setupFromOldDefaultProperties() {
    val ici = this.asInstanceOf[ICustomIcon]
    ici.setModName(getDefaultModName)
    ici.setSheetNumber(getDefaultSheetNumber)
  }

  @deprecated
  def getDefaultSheetNumber: Int = getDescriptorSheetNumber

  @deprecated
  def getDefaultModName: String = getDescriptorModName
}
