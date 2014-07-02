/*
 * Jaffas and more!
 * author: monnef
 */
package monnef.core.power

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import scala.collection.immutable.HashSet

object WrenchHelper {
  def registerWrenchItem(item: Item) {
    wrenches += item
  }

  def isPipeWrenchOrCompatible(item: Item): Boolean =
    if (item.isInstanceOf[IPipeWrench]) true
    else if (wrenches.contains(item)) true
    else false

  def isHoldingWrench(player: EntityPlayer): Boolean = {
    val hand: ItemStack = player.getHeldItem
    hand != null && isPipeWrenchOrCompatible(hand.getItem)
  }

  private var wrenches: HashSet[Item] = new HashSet[Item]
}