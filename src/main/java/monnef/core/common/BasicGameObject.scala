package monnef.core.common

import net.minecraft.block.Block
import net.minecraft.item.Item

abstract class BasicGameObject {
  def isBlock: Boolean

  def isItem: Boolean = !isBlock

  def asBlock() = this.asInstanceOf[BlockGameObject]

  def asItem() = this.asInstanceOf[ItemGameObject]
}

class BlockGameObject(val block: Block) extends BasicGameObject {
  override def isBlock: Boolean = true
}

class ItemGameObject(val item: Item) extends BasicGameObject {
  override def isBlock: Boolean = false
}

object BasicGameObject {
  def apply(block: Block) = new BlockGameObject(block)

  def apply(item: Item) = new ItemGameObject(item)
}
