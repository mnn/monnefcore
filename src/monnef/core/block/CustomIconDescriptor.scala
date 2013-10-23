/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.block

import monnef.core.api.ICustomIcon

trait CustomIconDescriptor {
  def getBlockDescriptorModName: String

  def getBlockDescriptorSheetNumber: Int

  def setupDefaultValuesFromBlockDescriptor(block: ICustomIcon) {
    block.setModName(getBlockDescriptorModName)
    block.setSheetNumber(getBlockDescriptorSheetNumber)
  }

  @deprecated
  def getDefaultSheetNumber: Int = getBlockDescriptorSheetNumber

  @deprecated
  def getDefaultModName: String = getBlockDescriptorModName
}
