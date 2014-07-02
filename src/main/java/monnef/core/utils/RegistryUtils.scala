/*
 * Jaffas and more!
 * author: monnef
 */
package monnef.core.utils

import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.LoaderState
import cpw.mods.fml.common.registry.{GameData, GameRegistry, LanguageRegistry}
import cpw.mods.fml.relauncher.ReflectionHelper
import monnef.core.MonnefCorePlugin
import monnef.core.api.IItemBlock
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import java.lang.reflect.Constructor
import monnef.core.MonnefCorePlugin.Log

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
    registerSubBlocks(block, titles)
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

      var itemCtor: Constructor[_ <: IItemBlock] = null
      itemCtor = itemclass.getConstructor(classOf[Block])
      i = itemCtor.newInstance(block).asInstanceOf[Item]
      if (names != null) {
        i.asInstanceOf[IItemBlock].setSubNames(names)
      }

      try {
        //GameData.getMain.registerBlock(block, name, modId)
        val mainGameDataField = ReflectionHelper.findField(classOf[GameData], "mainData")
        val mainGameData = mainGameDataField.get(null).asInstanceOf[GameData]
        val registerBlockMethod = ReflectionHelper.findMethod(classOf[GameData], mainGameData, Array("registerBlock"), classOf[Block], classOf[String], classOf[String])
        registerBlockMethod.invoke(mainGameData, block, blockName, null)
      } catch {
        case e: Exception =>
          Log.printSevere("Problem in registerMyBlock - Forge block registration")
          throw new RuntimeException(e)
      }

      GameRegistry.registerItem(i, blockName, null)
    }
    catch {
      case e: Exception =>
        Log.printSevere("Problem in registerMyBlock.")
        throw new RuntimeException(e)
    }
  }

  @deprecated(message = "no titles in code, use lang files")
  private def registerSubBlocks(block: Block, names: Array[String]) {
    for (ix <- 0 until names.length) {
      val multiBlockStack: ItemStack = new ItemStack(block, 1, ix)
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
    val inputItems: Array[ItemStack] = Array.fill(9) {input.copy}
    GameRegistry.addShapelessRecipe(outputBlock.copy, inputItems)
  }
}