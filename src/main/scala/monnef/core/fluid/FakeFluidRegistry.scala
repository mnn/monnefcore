package monnef.core.fluid

import net.minecraft.block.{BlockStaticLiquid, Block}
import net.minecraftforge.fluids.{IFluidBlock, Fluid}

object FakeFluidRegistry {
  private[this] var data = Map[Block, Fluid]()

  /**
   * Registers fake fluid.
   * @param fluid Fake fluid to register, getBlock returns what block shall be placed.
   * @param block Block in world which we will interact with.
   */
  def register(fluid: Fluid, block: Block) {
    data += block -> fluid
  }

  def get(block: Block): Option[Fluid] = data.get(block)

  def wrap(block: Block): Option[Block with IFluidBlock] =
    get(block) match {
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
}
