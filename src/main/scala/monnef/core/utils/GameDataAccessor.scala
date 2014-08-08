package monnef.core.utils

import cpw.mods.fml.common.registry.{FMLControlledNamespacedRegistry, GameData}
import cpw.mods.fml.relauncher.ReflectionHelper
import net.minecraft.block.Block
import net.minecraft.item.Item
import monnef.core.api.IItemBlock
import cpw.mods.fml.common.FMLLog

object GameDataAccessor {
  def getMain: GameData = {
    val mainGameDataField = ReflectionHelper.findField(classOf[GameData], "mainData")
    mainGameDataField.get(null).asInstanceOf[GameData]
  }

  def registerBlock(block: Block, blockName: String, modId: String): Int = {
    val mainGameData = getMain
    val registerBlockMethod = ReflectionHelper.findMethod(classOf[GameData], mainGameData, Array("registerBlock"), classOf[Block], classOf[String], classOf[String])
    registerBlockMethod.invoke(mainGameData, block, blockName, modId).asInstanceOf[Int]
  }

  def freeSlot(idHint: Integer, obj: Object) {
    freeSlot(getMain, idHint, obj)
  }

  def freeSlot(main: GameData, idHint: Integer, obj: Object) {
    val freeSlotMethod = ReflectionHelper.findMethod(classOf[GameData], main, Array("freeSlot"), classOf[Int], classOf[Object])
    freeSlotMethod.invoke(main, idHint, obj)
  }

  def useSlot(itemId: Integer) {
    val main = getMain
    val useSlotMethod = ReflectionHelper.findMethod(classOf[GameData], main, Array("useSlot"), classOf[Int])
    useSlotMethod.invoke(main, itemId)
  }

  def iItemRegistry: FMLControlledNamespacedRegistry[Item] = iItemRegistry(getMain)

  def iItemRegistry(main: GameData): FMLControlledNamespacedRegistry[Item] = {
    val field = ReflectionHelper.findField(classOf[GameData], "iItemRegistry")
    field.get(main).asInstanceOf[FMLControlledNamespacedRegistry[Item]]
  }

  def iItemRegistryAdd(idHint: Integer, name: String, item: Item, availabilityMap: java.util.BitSet): Int = {
    val itemReg = iItemRegistry
    val addMethod = ReflectionHelper.findMethod(classOf[FMLControlledNamespacedRegistry[Item]], itemReg, Array("add"), classOf[Int], classOf[String], classOf[Object], classOf[java.util.BitSet])
    addMethod.invoke(itemReg, idHint, name, item, availabilityMap).asInstanceOf[Int]
  }

  def availabilityMap: java.util.BitSet = {
    val field = ReflectionHelper.findField(classOf[GameData], "availabilityMap")
    field.get(getMain).asInstanceOf[java.util.BitSet]
  }

  def verifyCustomItemBlockName(item: IItemBlock) {
    verifyCustomItemBlockName(getMain, item)
  }

  def verifyCustomItemBlockName(main: GameData, item: IItemBlock) {
    val blockName: String = iBlockRegistry(main).getNameForObject(item.getBlock)
    val itemName: String = iItemRegistry(main).getNameForObject(item)

    if (blockName != null && !(blockName == itemName)) {
      FMLLog.bigWarning("Block <-> CustomItemBlock name mismatch, block name %s, item name %s", blockName, itemName)
    }
  }

  def iBlockRegistry(main: GameData): FMLControlledNamespacedRegistry[Block] = {
    val field = ReflectionHelper.findField(classOf[GameData], "iBlockRegistry")
    field.get(main).asInstanceOf[FMLControlledNamespacedRegistry[Block]]
  }
}
