package monnef.core.fluid

import net.minecraft.block.{BlockStaticLiquid, Block}
import net.minecraftforge.fluids.{IFluidBlock, Fluid}

object FakeFluidRegistry {
  private[this] var worldBlockToFluid = Map[Block, Fluid]()
  private[this] var fluidToBlockToPlace = Map[Fluid, Block]()

  /**
   * Registers fake fluid.
   * @param fluid Fake fluid to register, getBlock returns what block shall be placed.
   * @param worldBlock Block in world which we will interact with.
   */
  def register(fluid: Fluid, worldBlock: Block, toPlaceBlock: Block) {
    worldBlockToFluid += worldBlock -> fluid
    fluidToBlockToPlace += fluid -> toPlaceBlock
  }

  def getFluid(block: Block): Option[Fluid] = worldBlockToFluid.get(block)

  def wrap(block: Block): Option[Block with IFluidBlock] =
    getFluid(block) match {
      case Some(fluid) =>
        block match {
          case b: BlockStaticLiquid => Some(new StaticLiquidIFluidBlockProxy(b, fluid))
          case _ => None
        }
      case None => None
    }

  def wrapOrPass(block: Block): Block =
    wrap(block) match {
      case Some(b) => b
      case None => block
    }

  def getBlockToPlace(fluid: Fluid): Option[Block] = fluidToBlockToPlace.get(fluid)
}
