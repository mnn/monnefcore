package monnef.core.common

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.inventory.IInventory
import cpw.mods.fml.common.gameevent.PlayerEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent

/**
 * Generic crafting handler.
 * registration: FMLCommonHandler.instance().bus().register(new MyCraftingHandler());
 */
abstract class MonnefCoreCraftingHandler {
  def onCrafting(player: EntityPlayer, stack: ItemStack, craftMatrix: IInventory) {}

  def onSmelting(player: EntityPlayer, stack: ItemStack) {}

  @SubscribeEvent
  def crafting(evt: PlayerEvent.ItemCraftedEvent) {
    onCrafting(evt.player, evt.crafting, evt.craftMatrix)
  }

  @SubscribeEvent
  def smelting(evt: PlayerEvent.ItemSmeltedEvent) {
    onSmelting(evt.player, evt.smelting)
  }
}
