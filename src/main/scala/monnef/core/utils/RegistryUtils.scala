package monnef.core.utils

import net.minecraft.block.Block
import cpw.mods.fml.common.registry.{FMLControlledNamespacedRegistry, GameData, LanguageRegistry, GameRegistry}
import net.minecraft.item.{ItemStack, Item, ItemBlock}
import monnef.core.api.IItemBlock
import cpw.mods.fml.common.{FMLLog, LoaderState, Loader}
import monnef.core.MonnefCorePlugin._
import java.lang.reflect.Constructor
import cpw.mods.fml.relauncher.ReflectionHelper
import monnef.core.MonnefCorePlugin

object RegistryUtils {
  def registerBlock(block: Block) {
    GameRegistry.registerBlock(block, block.getUnlocalizedName)
  }

  def registerBlockWithName(block: Block, name: String) {
    registerBlock(block)
    block.setBlockName(name)
  }

  @deprecated(message = "no titles in code, use lang files")
  def registerBlock(block: Block, title: String) {
    registerBlock(block)
    LanguageRegistry.addName(block, title)
  }

  @deprecated(message = "no titles in code, use lang files")
  def registerBlock(block: Block, name: String, title: String) {
    block.setBlockName(name)
    registerBlock(block, title)
  }

  @deprecated(message = "no titles in code, use lang files")
  def registerMultiBlock(block: Block, itemBlock: Class[_], titles: Array[String]) {
    registerMultiBlock(block, itemBlock, titles, null)
  }

  @deprecated(message = "no titles in code, use lang files")
  def registerMultiBlock(block: Block, itemBlock: Class[_], name: String, title: String) {
    registerMultiBlock(block, itemBlock, Array[String](title), Array[String](name))
  }

  @deprecated(message = "no titles in code, use lang files")
  def registerMultiBlock(block: Block, itemBlock: Class[_], titles: Array[String], names: Array[String]) {
    if (block.getUnlocalizedName == null && (names == null || names.length <= 0)) {
      throw new RuntimeException("Cannot find a block name - " + itemBlock.getSimpleName + ".")
    }
    val cls: Class[_] = itemBlock
    if (classOf[ItemBlock].isAssignableFrom(cls)) {
      if (names != null && names.length != 1) {
        throw new RuntimeException("cannot have multiple custom names for ItemBlock")
      }
      block.setBlockName(names(0))
      GameRegistry.registerBlock(block, itemBlock.asInstanceOf[Class[_ <: ItemBlock]], block.getUnlocalizedName)
    }
    else if (classOf[IItemBlock].isAssignableFrom(cls)) {
      registerMyBlock(block, itemBlock.asInstanceOf[Class[_ <: IItemBlock]], block.getUnlocalizedName, names)
    }
    else {
      throw new RuntimeException("Unknown class in block registration.")
    }
    registerSubBlockNames(block, titles)
  }

  private def registerMyBlock(block: Block, itemclass: Class[_ <: IItemBlock], blockName: String, names: Array[String]) {
    // heavily based on GameRegistry.registerBlock of Forge
    if (Loader.instance.isInState(LoaderState.CONSTRUCTING)) {
      Log.printWarning("Registering block in non-constructing state!")
    }
    var i: Item = null
    try {
      assert(block != null, "registerBlock: block cannot be null")
      assert(itemclass != null, "registerBlock: itemclass cannot be null")

      try {
        var itemCtor: Constructor[_ <: IItemBlock] = null
        itemCtor = itemclass.getConstructor(classOf[Block])
        i = itemCtor.newInstance(block).asInstanceOf[Item]
      } catch {
        case _: NoSuchMethodException =>
          try {
            i = itemclass.newInstance().asInstanceOf[Item]
          } catch {
            case e: NoSuchMethodException =>
              throw new RuntimeException("Block doesn't have suitable constructor (either parameter-less or accepting a block).", e)
          }
      }

      if (names != null) {
        i.asInstanceOf[IItemBlock].setSubNames(names)
      }

      try {
        //GameData.getMain.registerBlock(block, name, modId)
        GameDataAccessor.registerBlock(block, blockName, null)
      } catch {
        case e: Exception =>
          Log.printSevere("Problem in registerMyBlock - Forge block registration")
          throw new RuntimeException(e)
      }

      //GameRegistry.registerItem(i, blockName, null)
      registerCustomItemBlock(i, blockName)
    }
    catch {
      case e: Exception =>
        Log.printSevere("Problem in registerMyBlock.")
        throw new RuntimeException(e)
    }
  }

  private def registerCustomItemBlock(item: Item, name: String): Int = {
    val block = item.asInstanceOf[IItemBlock].getBlock
    val idHint: Integer = GameData.getBlockRegistry.getId(block)
    if (idHint == -1) {
      throw new RuntimeException("Block must be registered first.")
    }
    else {
      FMLLog.fine("[registerCustomItemBlock] Found matching Block %s for ItemBlock %s at id %d", block, item, idHint)
      GameDataAccessor.freeSlot(idHint, item)
      if (GameData.getItemRegistry.getObjectById(idHint) != null) throw new RuntimeException("freeSlot was unsuccessful")
    }

    val itemId: Int = GameDataAccessor.iItemRegistryAdd(idHint, name, item, GameDataAccessor.availabilityMap)
    if (itemId != idHint) throw new IllegalStateException("Block -> ItemBlock insertion failed.")
    GameDataAccessor.verifyCustomItemBlockName(item.asInstanceOf[IItemBlock])
    GameDataAccessor.useSlot(itemId)
    itemId
  }

  @deprecated(message = "no titles in code, use lang files")
  def registerSubBlockNames(block: Block, names: Array[String]) {
    for (ix <- 0 until names.length) {
      val blockId = GameData.getBlockRegistry.getId(block)
      val multiBlockStack: ItemStack = new ItemStack(Item.getItemById(blockId), 1, ix)
      if (multiBlockStack.getItem == null) {
        throw new RuntimeException("Item from " + block.getUnlocalizedName + " Block is null!")
      }
      LanguageRegistry.addName(multiBlockStack, names(multiBlockStack.getItemDamage))
    }
  }

  @deprecated(message = "no titles in code, use lang files")
  def registerSubItems(item: Item, names: Array[String]) {
    for (ix <- 0 until names.length) {
      val multiBlockStack: ItemStack = new ItemStack(item, 1, ix)
      LanguageRegistry.addName(multiBlockStack, names(multiBlockStack.getItemDamage))
    }
  }

  @deprecated(message = "no titles in code, use lang files")
  def registerItem[T <: Item](item: T, name: String, title: String): T = {
    item.setUnlocalizedName(name)
    GameRegistry.registerItem(item, item.getUnlocalizedName)
    LanguageRegistry.addName(item, title)
    item
  }

  def registerItem[T <: Item](item: T): T = {
    GameRegistry.registerItem(item, item.getUnlocalizedName)
    item
  }

  def registerItem[T <: Item](item: T, name: String): T = {
    item.setUnlocalizedName(name)
    registerItem(item)
  }

  def getTitle(stack: ItemStack): String = {
    // TODO needed? it's pre 1.7, might be broken
    var title: String = stack.getDisplayName
    if (title == null || title.isEmpty || title.contains("item.") || title.contains("tile.") || title.contains(".name")) {
      title = LanguageRegistry.instance.getStringLocalization(stack.getItem.getUnlocalizedName + ".name")
    }
    if (title == null || title.isEmpty) {
      title = stack.getUnlocalizedName
      MonnefCorePlugin.Log.printWarning(String.format("Unable to find translation of %s.", title))
    }
    title
  }

  def registerBlockPackingRecipe(input: ItemStack, outputBlock: ItemStack) {
    if (input == null || input.getItem == null || outputBlock == null || outputBlock.getItem == null) {
      throw new NullPointerException
    }
    if (Item.getIdFromItem(input.getItem) == 0) {
      throw new RuntimeException(s"Input ${input.getItem.getUnlocalizedName} has invalid ID.")
    }
    if (Item.getIdFromItem(outputBlock.getItem) == 0) {
      throw new RuntimeException(s"Output ${outputBlock.getItem.getUnlocalizedName} has invalid ID.")
    }
    val inputItems: Array[ItemStack] = Array.fill(9) {input.copy}
    GameRegistry.addShapelessRecipe(outputBlock.copy, inputItems: _*)
  }
}
