package monnef.core.utils

import net.minecraft.block.Block
import cpw.mods.fml.common.registry.{FMLControlledNamespacedRegistry, GameData, LanguageRegistry, GameRegistry}
import net.minecraft.item.{ItemStack, Item, ItemBlock}
import monnef.core.api.IItemBlock
import cpw.mods.fml.common.{FMLCommonHandler, FMLLog, LoaderState, Loader}
import monnef.core.MonnefCorePlugin._
import java.lang.reflect.Constructor
import cpw.mods.fml.relauncher.ReflectionHelper
import monnef.core.MonnefCorePlugin
import net.minecraft.client.resources.I18n
import scalautils._
import net.minecraft.util.StatCollector

object RegistryUtils {
  def registerBlock(block: Block) {
    GameRegistry.registerBlock(block, block.getUnlocalizedName)
  }

  def registerBlockWithName(block: Block, name: String) {
    block.setBlockName(name)
    registerBlock(block)
  }

  def registerMultiBlock(block: Block, itemBlock: Class[_], name: String) {
    registerMultiBlock(block, itemBlock, Array[String](name))
  }

  def registerMultiBlock(block: Block, itemBlock: Class[_], names: Array[String]) {
    if (block.getUnlocalizedName == null && (names == null || names.length <= 0)) {
      throw new RuntimeException("Cannot find a block name - " + itemBlock.getSimpleName + ".")
    }
    names.foreach { case name => if (name.contains(" ") || name(0).toLower != name(0)) Log.printWarning(s"Invalid name '$name' used for registration of multi block ${block.getUnlocalizedName} (${block.getClass}})")}
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
        //GameData.getMain.registerBlock(block, name,)
        GameDataAccessor.registerBlock(block, blockName)
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

  def registerItem[T <: Item](item: T): T = {
    val name = item.getUnlocalizedName
    if ("item.null".equals(name)) throw new RuntimeException("Trying to register not named item, this will cause unpredictable errors if not fixed.")
    GameRegistry.registerItem(item, name)
    item
  }

  def registerItem[T <: Item](item: T, name: String): T = {
    item.setUnlocalizedName(name)
    registerItem(item)
  }

  private val titleGetters: Seq[(ItemStack) => (String, String)] = Seq(
    s => "D" -> s.getDisplayName,
    s => "s" -> StatCollector.translateToLocal(s.getUnlocalizedName),
    s => "S" -> StatCollector.translateToLocal(s.getUnlocalizedName + ".name"),
    s => "i" -> I18n.format(s.getUnlocalizedName),
    s => "I" -> I18n.format(s.getUnlocalizedName + ".name"),
    s => "l" -> (try {LanguageRegistry.instance().getStringLocalization(s.getUnlocalizedName)} catch {case _: Throwable => null}),
    s => "L" -> (try {LanguageRegistry.instance().getStringLocalization(s.getUnlocalizedName + ".name")} catch {case _: Throwable => null}),
    s => "u" -> LanguageRegistry.instance().getStringLocalization(s.getUnlocalizedName, "en_US"),
    s => "U" -> LanguageRegistry.instance().getStringLocalization(s.getUnlocalizedName + ".name", "en_US"),

    s => "?" -> ("[???]" + s.getUnlocalizedName)
  )

  def getTitle(stack: ItemStack): String = {
    if (stack == null) throw new RuntimeException("Null stack.")
    if (stack.getItem == null) throw new RuntimeException("Null item in stack.")
    def seemsInvalid(name: String): Boolean = name == null || "".equals(name) || name.startsWith("item.") || name.startsWith("tile.") || name.endsWith(".name")

    titleGetters.foldLeft(("", "")) { case (acc, f) => if (seemsInvalid(acc._2)) f(stack) else acc} |> { case (id, t) => (if (MonnefCorePlugin.debugEnv) id else "") + t}
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
