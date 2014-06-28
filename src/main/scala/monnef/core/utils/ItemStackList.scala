/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils

import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.OreDictionary
import scala._
import monnef.core.MonnefCorePlugin

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
    db exists { i => ItemHelper.haveStacksSameIdAndDamage(i, stack)}

  def add(s: ItemStack) { db += s }

  /*
  private def translateItemId(id: Int): Int =
      if (id <= Block.blocksList.length) id
      else id + ITEM_ID_SHIFT
  */

  def add(item: Item, meta: Int = OreDictionary.WILDCARD_VALUE) { add(new ItemStack(item, 1, meta)) }

  private def translateMeta(meta: Int): Int =
    meta match {
      case ANY_META => OreDictionary.WILDCARD_VALUE
      case _ => meta
    }

  def addWithTranslatedMeta(item: Item, meta: Int = ANY_META) { add(item, translateMeta(meta)) }

  override def toString = {
    def formattedDamage(v: Int): Int = if (v == OreDictionary.WILDCARD_VALUE) -1 else v
    name + ": " + (for {i <- db} yield s"${Item.getIdFromItem(i.getItem)}:${formattedDamage(i.getItemDamage)}").mkString(", ")
  }

  def printToLog(log: CustomLogger) { log.printInfo(s"${this.getClass.getSimpleName} - $toString") }

  def loadFromString(s: String): ItemStackList = {
    clear()
    val itemNamesWithMeta: Seq[Seq[String]] = s.replaceAll(" |\n", "").split(",|;").toList.map(_.split(":").toList)
    if (s.isEmpty || (itemNamesWithMeta.length == 1 && itemNamesWithMeta(0)(0).isEmpty)) {
      // nothing to process
    } else {
      def validate(a: Seq[Seq[String]]) {
        for {part <- a} {
          val formattedPart = part.mkString("[", ",", "]")
          if (part.length != 1 && part.length != 2) throw new RuntimeException(s"$name: Invalid length of item after splitting: '$formattedPart'.")
          def stringIsInteger(subPart: String): Boolean = subPart.forall(c => c.isDigit || c == '-')
          if (part.length == 2 && !stringIsInteger(part(1))) throw new RuntimeException(s"$name: Meta part doesn't seem to be an integer: '${part(1)}'.")
        }
      }
      validate(itemNamesWithMeta)
      val itemsWithMeta = itemNamesWithMeta.map {
        (a: Seq[String]) => {
          def constructItem(name: String): Item = Item.itemRegistry.getObject(name).asInstanceOf[Item]
          if (a.length == 1) (constructItem(a(0)), ANY_META)
          else if (a.length == 2) (constructItem(a(0)), a(1).toIntOpt.get)
          else throw new RuntimeException
        }: (Item, Int)
      }
      itemsWithMeta foreach {
        case (item: Item, meta: Int) =>
          if (MonnefCorePlugin.debugEnv) MonnefCorePlugin.Log.printFine(s"$name: Adding - '${item.getUnlocalizedName}:$meta'.")
          addWithTranslatedMeta(item, meta)
      }
    }
    this
  }

  def clear() = db = Set()

  def size = db.size
}
