package monnef.core.fluid

import net.minecraft.block.{BlockStaticLiquid, Block}
import net.minecraftforge.fluids._
import monnef.core.api.IIntegerCoordinates
import net.minecraft.world.World
import monnef.core.utils.BlockHelper
import net.minecraft.util.MathHelper
import monnef.jaffas.food.JaffasFood
import monnef.core.utils.scalagameutils._

object FluidPlacer {
  def unwrapProxiedOrPass(block: Block): Block = block match {
    case b: IFluidBlockProxy => b.innerBlock
    case b => b
  }

  def tryPlace(fluidStack: FluidStack, pos: IIntegerCoordinates): Boolean = tryPlace(fluidStack, pos.getWorld, pos.getX, pos.getY, pos.getZ)

  def place(fluidStack: FluidStack, world: World, x: Int, y: Int, z: Int) {
    val blockToPlace = fluidStack.getFluid.getBlock

    if (!world.isRemote) {
      blockToPlace match {
        case b: BlockStaticLiquid =>
          FakeFluidRegistry.getBlockToPlace(fluidStack.getFluid) match {
            case Some(translatedBlockToPlace) => BlockHelper.setBlock(world, x, y, z, translatedBlockToPlace)
            case None => JaffasFood.Log.printSevere(s"Attempt to place a vanilla liquid block without proper mapping: ${blockToPlace.getUnlocalizedName}.")
          }

        case b: BlockFluidClassic => BlockHelper.setBlock(world, x, y, z, b, b.getMaxMeta())

        case b: BlockFluidFinite =>
          val meta: Int = MathHelper.floor_float((fluidStack.amount.asInstanceOf[Float] / FluidContainerRegistry.BUCKET_VOLUME) * b.getQuantaPerBlock) - 1
          BlockHelper.setBlock(world, x, y, z, b, meta)

        case _ => JaffasFood.Log.printSevere(s"Attempt to place an unknown liquid block: ${blockToPlace.getUnlocalizedName}.")
      }
    }
  }

  def tryPlace(fluidStack: FluidStack, world: World, x: Int, y: Int, z: Int): Boolean = {
    val wholeBucket = fluidStack.amount == FluidContainerRegistry.BUCKET_VOLUME
    if (world.isAirBlock(x, y, z) && (isFluidFinite(fluidStack.getFluid) || wholeBucket)) {
      place(fluidStack, world, x, y, z)
      true
    } else false
  }

  def isFluidFinite(fluid: Fluid): Boolean = fluid.getBlock.isInstanceOf[BlockFluidFinite]
}
