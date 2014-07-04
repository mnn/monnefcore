package monnef.core.utils

import net.minecraft.block.Block
import cpw.mods.fml.common.registry.GameData
import net.minecraft.item.Item

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
}
