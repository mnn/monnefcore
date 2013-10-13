/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import scala.collection.JavaConverters._
import java.lang.Integer
import scala.collection.mutable.ListBuffer

object CraftingHelper {

  object EnumReturnMode extends Enumeration {
    type EnumReturnMode = Value
    val NothingPreferred, CraftingMatrixPreferred, PlayerInventoryPreferred = Value
  }

  import monnef.core.utils.CraftingHelper.EnumReturnMode._

  def getFreeSlot(matrix: IInventory): Int = {
    for {
      i <- 0 to matrix.getSizeInventory
      if matrix.getStackInSlot(i) == null
    } return i
    -1
  }

  /**
   * Places items either to a crafting matrix or player's inventory
   * @param stacks Input items.
   * @param craftMatrix Crafting matrix.
   * @param player A player who is doing the crafting.
   * @param transferItemsDirectlyToPlayer Is preferred to return items to player's inventory (can be later overwritten when player is faulty).
   */
  def returnLeftovers(stacks: Seq[ItemStack], craftMatrix: IInventory, player: EntityPlayer, transferItemsDirectlyToPlayer: Boolean): Seq[Int] =
    returnLeftovers(stacks, craftMatrix, player, if (transferItemsDirectlyToPlayer) EnumReturnMode.PlayerInventoryPreferred else EnumReturnMode.CraftingMatrixPreferred)

  def returnLeftover(stack: ItemStack, craftMatrix: IInventory, player: EntityPlayer, transferItemsDirectlyToPlayer: Boolean): Option[Int] =
    returnLeftovers(List(stack), craftMatrix, player, transferItemsDirectlyToPlayer).headOption

  def returnLeftovers(stacks: java.util.List[ItemStack], craftMatrix: IInventory, player: EntityPlayer, transferItemsDirectlyToPlayer: Boolean): java.util.Collection[Integer] =
    returnLeftovers(stacks.asScala, craftMatrix, player, transferItemsDirectlyToPlayer).map(_.asInstanceOf[Integer]).asJava

  private val forceMatrixPlayerPackages = List(".immibis.")

  private def shouldGoToMatrix(craftMatrix: IInventory, player: EntityPlayer): Boolean = {
    val playerClassName = player.getClass.getName
    forceMatrixPlayerPackages.exists(playerClassName.contains)
  }

  def returnLeftovers(stacks: Seq[ItemStack], craftMatrix: IInventory, player: EntityPlayer, preferredMode: EnumReturnMode = EnumReturnMode.NothingPreferred): Seq[Int] = {
    val canGiveToPlayer = player != null && player.inventory != null && !shouldGoToMatrix(craftMatrix, player)
    val wantGiveToPlayer = preferredMode == EnumReturnMode.PlayerInventoryPreferred

    if (wantGiveToPlayer && canGiveToPlayer) returnLeftoversToPlayer(stacks, craftMatrix, player)
    else returnLeftoversToCraftingMatrix(stacks, craftMatrix, player)
  }

  /*
  def returnLeftover(stack: ItemStack, craftMatrix: IInventory, player: EntityPlayer, preferredMode: EnumReturnMode = EnumReturnMode.NothingPreferred) {
    returnLeftovers(List(stack), craftMatrix, player, preferredMode)
  }
  */

  def returnLeftoversToPlayer(stacks: Seq[ItemStack], craftMatrix: IInventory, player: EntityPlayer): Seq[Int] = {
    for (stack <- stacks) {
      PlayerHelper.giveItemToPlayer(player, stack)
    }
    List.empty
  }

  def returnLeftoversToCraftingMatrix(stacks: Seq[ItemStack], craftMatrix: IInventory, player: EntityPlayer): Seq[Int] = {
    val ret = new ListBuffer[Int]
    for (stack <- stacks) {
      val freeSlot = getFreeSlot(craftMatrix)
      if (freeSlot == -1) throw new RuntimeException("No free slot for leftovers.")
      val newStack = stack.copy
      newStack.stackSize += 1 // the crafting table will decrease it by 1
      craftMatrix.setInventorySlotContents(freeSlot, newStack)
      ret += freeSlot
    }
    ret
  }
}
