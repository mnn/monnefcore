package monnef.core.utils

import cpw.mods.fml.common.registry.GameData
import net.minecraft.block.Block
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import monnef.core.MonnefCorePlugin.Log

object BreakableIronMaterial {
  def onPostLoad() {
    if (initialized) throw new RuntimeException("re-initialization!")
    initialized = true

    val blocksFromBreakableIron = GameDataHelper.extractAllBlocks().filter { b => b != null && b.getMaterial == breakableIronMaterial}
    blocksFromBreakableIron.foreach {_.setHarvestLevel("pickaxe", 0)}
    val marked = blocksFromBreakableIron.size

    Log.printFine("Registered " + marked + " blocks as mine-able by pickaxe.")
    if (marked <= 0) {
      Log.printWarning("No block registered as mine-able by pickaxe, possible error!")
    }
  }

  private var initialized: Boolean = false
  final val breakableIronMaterial: Material = new Material(MapColor.ironColor)
}