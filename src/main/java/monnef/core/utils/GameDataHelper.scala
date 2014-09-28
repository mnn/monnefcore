package monnef.core.utils

import net.minecraft.block.Block
import cpw.mods.fml.common.registry.GameData
import net.minecraft.item.{ItemStack, Item}
import scala.collection.JavaConversions
import monnef.core.MonnefCorePlugin

object GameDataHelper {
  def extractAllBlocks(): Seq[Block] = {
    val i = GameData.getBlockRegistry.iterator()
    val r = scala.collection.mutable.ArrayBuffer[Block]()
    while (i.hasNext) r.append(i.next().asInstanceOf[Block])
    r
  }

  def extractAllItems(): Seq[Item] = {
    val i = GameData.getItemRegistry.iterator()
    val r = scala.collection.mutable.ArrayBuffer[Item]()
    while (i.hasNext) r.append(i.next().asInstanceOf[Item])
    r
  }

  def extractAllItemStacks(): Seq[ItemStack] = {
    extractAllItemStacksRaw().map {_._2}.flatten.filter(_ != null)
  }

  def extractAllItemStacksRaw(): Seq[(Item, Seq[ItemStack])] = {
    val nullRecord = (null, Seq[ItemStack]())
    extractAllItems().map {
      item =>
        if (item != null) {
          val tmpList = new java.util.ArrayList[ItemStack]()
          var ret: (Item, Seq[ItemStack]) = nullRecord
          try {
            item.getSubItems(item, null, tmpList)
            ret = (item, JavaConversions.asScalaBuffer(tmpList).toSeq)
          } catch {
            case _: Throwable => MonnefCorePlugin.Log.printWarning(s"Item ${item.getUnlocalizedName} crashed on getSubItems.")
          }
          ret
        } else {
          nullRecord
        }
    }
  }
}
