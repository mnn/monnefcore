package monnef.core.fluid

import net.minecraft.block.BlockStaticLiquid
import net.minecraftforge.fluids.{FluidContainerRegistry, FluidStack, Fluid}
import net.minecraft.world.World
import monnef.core.utils.BlockHelper

class StaticLiquidIFluidBlockProxy(override val innerBlock: BlockStaticLiquid, fluid: Fluid) extends IFluidBlockProxy(innerBlock, fluid) {
  override def drain(world: World, x: Int, y: Int, z: Int, doDrain: Boolean): FluidStack = {
    if (world.getBlock(x, y, z) != innerBlock) {
      emptyFluidStack
    } else {
      if (doDrain && !world.isRemote) BlockHelper.setAir(world, x, y, z)
      new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME)
    }
  }

  override def getFilledPercentage(world: World, x: Int, y: Int, z: Int): Float = 1
}
