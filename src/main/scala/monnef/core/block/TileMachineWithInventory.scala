package monnef.core.block

import monnef.core.common.ContainerRegistry
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import monnef.core.utils.NBTHelper

abstract class TileMachineWithInventory extends TileMachine with IInventory {
  protected var inventory: Array[ItemStack] = null
  var guiPowerMax: Int = _
  var guiPowerStored: Int = _
  private var containerDescriptor: ContainerRegistry.ContainerDescriptor = null

  setupContainerDescriptor()
  inventory = new Array[ItemStack](getSizeInventory())

  def getGuiPowerMax = guiPowerMax

  def getGuiPowerStored = guiPowerStored

  private def setupContainerDescriptor(): Unit = {
    containerDescriptor = ContainerRegistry.getContainerPrototype(this.getClass)
  }

  def getContainerDescriptor: ContainerRegistry.ContainerDescriptor = containerDescriptor

  override def getSizeInventory: Int = containerDescriptor.getSlotsCount

  override def getStackInSlot(slot: Int): ItemStack = inventory(slot)

  override def setInventorySlotContents(slot: Int, stack: ItemStack): Unit = {
    inventory(slot) = stack
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit()
    }
    markDirty()
  }

  override def decrStackSize(slot: Int, amt: Int): ItemStack = {
    var stack: ItemStack = getStackInSlot(slot)
    if (stack != null) {
      if (stack.stackSize <= amt) {
        setInventorySlotContents(slot, null)
      } else {
        stack = stack.splitStack(amt)
        if (stack.stackSize == 0) {
          setInventorySlotContents(slot, null)
        }
      }
    }
    stack
  }

  override def getStackInSlotOnClosing(slot: Int): ItemStack = {
    val stack: ItemStack = getStackInSlot(slot)
    if (stack != null) {
      setInventorySlotContents(slot, null)
    }
    stack
  }

  override def getInventoryStackLimit: Int = 64

  override def isUseableByPlayer(player: EntityPlayer): Boolean = {
    worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64
  }

  override def openInventory(): Unit = {
  }

  override def closeInventory(): Unit = {
  }

  override def readFromNBT(tagCompound: NBTTagCompound): Unit = {
    super.readFromNBT(tagCompound)
    val tagList: NBTTagList = tagCompound.getTagList("Inventory", NBTHelper.TagTypes.TAG_Compound)

    var i: Int = 0
    while (i < tagList.tagCount()) {
      val tag: NBTTagCompound = tagList.getCompoundTagAt(i)
      val slot: Byte = tag.getByte("Slot")
      if (slot >= 0 && slot < inventory.length) {
        inventory(slot) = ItemStack.loadItemStackFromNBT(tag)
      }

      i = i + 1
    }
    markDirty()
  }

  override def writeToNBT(tagCompound: NBTTagCompound): Unit = {
    super.writeToNBT(tagCompound)
    val itemList: NBTTagList = new NBTTagList()

    var i: Int = 0
    while (i < inventory.length) {
      var stack: ItemStack = inventory(i)
      if (stack != null) {
        var tag: NBTTagCompound = new NBTTagCompound()
        tag.setByte("Slot", i.asInstanceOf[Byte])
        stack.writeToNBT(tag)
        itemList.appendTag(tag)
      }

      i = i + 1
    }
    tagCompound.setTag("Inventory", itemList)
  }

  override def getInventoryName: String

  def getIntegersToSyncCount: Int = 2

  def getCurrentValueOfIntegerToSync(index: Int): Int = {
    index match {
      case 0 => getPowerHandler.getEnergyStored.asInstanceOf[Int]
      case 1 => getPowerHandler.getMaxEnergyStored.asInstanceOf[Int]
      case _ => -1
    }
  }

  def setCurrentValueOfIntegerToSync(index: Int, value: Int): Unit = {
    index match {
      case 0 => guiPowerStored = value
      case 1 => guiPowerMax = value
      case _ =>
    }
  }

  override def hasCustomInventoryName: Boolean = false

  override def isItemValidForSlot(i: Int, itemstack: ItemStack): Boolean = true

  def isPowerBarRenderingEnabled: Boolean = true

  protected def addItemToInventory(stack: ItemStack, doAdd: Boolean): Int = {
    var free: Int = -1
    var addToStack: Boolean = false
    var ret: Int = -1
    val slotsCount: Int = getSizeInventory()

    {
      var i: Int = 0
      while (i < slotsCount) {
        if (getStackInSlot(i) == null) {
          free = i
          i = slotsCount
        } else
        if (getStackInSlot(i).getItem == stack.getItem && getStackInSlot(i).stackSize < getStackInSlot(i).getMaxStackSize()) {
          addToStack = true
          free = i
          i = slotsCount
        }
        i = i + 1
      }
    }

    if (free != -1) {
      if (addToStack) {
        val newStackSize: Int = stack.stackSize + getStackInSlot(free).stackSize
        if (doAdd)
          getStackInSlot(free).stackSize += stack.stackSize
        if (newStackSize > stack.getMaxStackSize) {
          val overflowItemsCount: Int = newStackSize % stack.getMaxStackSize
          if (doAdd)
            getStackInSlot(free).stackSize = stack.getMaxStackSize
          val c: ItemStack = stack.copy()
          c.stackSize = overflowItemsCount
          ret = stack.stackSize - overflowItemsCount
          ret += addItemToInventory(c, doAdd)
        } else {
          ret = stack.stackSize
        }
      } else {
        if (doAdd)
          setInventorySlotContents(free, stack)
        ret = stack.stackSize
      }

    } else {
      ret = 0
    }

    ret
  }

  def canAddToInventory(item: EntityItem): Boolean = {
    this.addItemToInventory(item.getEntityItem, doAdd = false) > 0
  }


}
