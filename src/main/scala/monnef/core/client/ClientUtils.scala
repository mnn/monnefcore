package monnef.core.client

import net.minecraftforge.client.{MinecraftForgeClient, IItemRenderer}
import net.minecraft.item.Item
import net.minecraft.block.Block

object ClientUtils {
  def registerItemRendererOfBlock(block: Block, renderer: IItemRenderer) {
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), renderer)
  }
}
