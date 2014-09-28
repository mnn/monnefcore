package monnef.core.utils

import net.minecraft.util.StatCollector

object LanguageHelper {
  def toLocal(name: String): String = StatCollector.translateToLocal(name)

  def toLocalFormatted(name: String, args: AnyRef*): String = StatCollector.translateToLocalFormatted(name, args: _*)

  def localInventory: String = toLocal("container.inventory")
}
