package monnef.core.fluid

import net.minecraft.block.Block
import net.minecraftforge.fluids.{FluidStack, IFluidBlock, Fluid}
import net.minecraft.block.material.Material
import net.minecraft.world.World

/**
 * Created by moen on 30/08/2014.
 */
abstract class IFluidBlockProxy(val innerBlock: Block, fluid: Fluid) extends Block(Material.water) with IFluidBlock {
  override def getFluid: Fluid = fluid

  override def canDrain(world: World, x: Int, y: Int, z: Int): Boolean = true

  def emptyFluidStack = new FluidStack(fluid, 0)
}
