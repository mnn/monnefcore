/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils

import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import scala._
import monnef.core.MonnefCorePlugin
import net.minecraft.block.Block

object ItemStackList {
  val ANY_META: Int = -1

  val ITEM_ID_SHIFT = 256

  def fromString(text: String, name: String): ItemStackList = new ItemStackList(name) loadFromString text
}

class ItemStackList(val name: String) {

  import ItemStackList._
  import monnef.core.utils.scalautils._

  private[this] var db = Set[ItemStack]()

  def contains(stack: ItemStack): Boolean =
    db exists {i => ItemHelper.haveStacksSameIdAndDamage(i, stack)}

  def add(s: ItemStack) { db += s }

  private def translateItemId(id: Int): Int =
    if (id <= Block.blocksList.length) id
    else id + ITEM_ID_SHIFT

  def add(id: Int, meta: Int = OreDictionary.WILDCARD_VALUE) { add(new ItemStack(translateItemId(id), 1, meta)) }

  private def translateMeta(meta: Int): Int =
    meta match {
      case ANY_META => OreDictionary.WILDCARD_VALUE
      case _ => meta
    }

  def addWithTranslatedMeta(id: Int, meta: Int = ANY_META) { add(id, translateMeta(meta)) }

  override def toString = {
    def formattedDamage(v: Int): Int = if (v == OreDictionary.WILDCARD_VALUE) -1 else v
    name + ": " + (for {i <- db} yield s"${i.itemID}:${formattedDamage(i.getItemDamage)}").mkString(", ")
  }

  def printToLog(log: CustomLogger) { log.printInfo(s"${this.getClass.getSimpleName} - $toString") }

  def loadFromString(s: String): ItemStackList = {
    clear()
    val idsWithMeta: Seq[Seq[String]] = s.replaceAll(" |\n", "").split(",|;").toList.map(_.split(":").toList)
    if (s.isEmpty || (idsWithMeta.length == 1 && idsWithMeta(0)(0).isEmpty)) {
      // nothing to process
    } else {
      def validate(a: Seq[Seq[String]]) {
        for {part <- a} {
          val formattedPart = part.mkString("[", ",", "]")
          if (part.length != 1 && part.length != 2) throw new RuntimeException(s"$name: Invalid length of item after splitting: '$formattedPart'.")
          for {subPart <- part} if (!subPart.forall(c => c.isDigit || c == '-')) throw new RuntimeException(s"$name: Subpart doesn't seem to be an integer: '$subPart'.")
        }
      }
      validate(idsWithMeta)
      idsWithMeta.map {
        (a: Seq[String]) => {
          if (a.length == 1) (a(0).toIntOpt.get, ANY_META)
          else if (a.length == 2) (a(0).toIntOpt.get, a(1).toIntOpt.get)
          else throw new RuntimeException
        }: (Int, Int)
      } foreach {
        case (id: Int, meta: Int) => {
          if (MonnefCorePlugin.debugEnv) MonnefCorePlugin.Log.printFine(s"$name: Adding - '$id:$meta'.")
          addWithTranslatedMeta(id, meta)
        }
      }
    }
    this
  }

  def clear() = db = Set()

  def size = db.size
}
