/*
 * Jaffas and more!
 * author: monnef
 */
package monnef.core.utils

import net.minecraft.block.Block
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import scala.collection.immutable.HashSet
import net.minecraft.init.Blocks

object BlockHelper {
  private var winterBlocks: HashSet[Block] = HashSet(Blocks.snow, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice)

  final val NOTIFY_FLAG: Int = 2
  final val SEND_ID_OF_CHANGED_BLOCK_FLAG: Int = 1
  final val NOT_UPDATE_ON_CLIENT: Int = 4
  final val NOTIFY_NONE: Int = 0
  final val NOTIFY_ALL: Int = NOTIFY_FLAG | SEND_ID_OF_CHANGED_BLOCK_FLAG

  def setBlockMetadata(world: World, x: Int, y: Int, z: Int, metadata: Int): Boolean = world.setBlockMetadataWithNotify(x, y, z, metadata, NOTIFY_ALL)

  def setBlock(world: World, x: Int, y: Int, z: Int, block: Block): Boolean = world.setBlock(x, y, z, block)

  def setAir(world: World, x: Int, y: Int, z: Int): Boolean = world.setBlockToAir(x, y, z)

  def setBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int): Boolean = world.setBlock(x, y, z, block, meta, NOTIFY_ALL)

  def setBlockWithoutNotify(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int): Boolean = world.setBlock(x, y, z, block, meta, NOTIFY_NONE)

  def isWinterBlock(block: Block): Boolean = winterBlocks.contains(block)

  private def getCurrentLocation(coord: Int, addCurrentLocation: Boolean): Double = if (addCurrentLocation) coord else 0

  def rotateBoundingBoxCoordinates(box: BoundingBoxSize, rotation: Int, x: Int, y: Int, z: Int, addCurrentLocation: Boolean): AxisAlignedBB = {
    val bbx = getCurrentLocation(x, addCurrentLocation) + box.x1
    val bby = getCurrentLocation(y, addCurrentLocation) + box.y1
    val bbz = getCurrentLocation(z, addCurrentLocation) + box.z1
    val bbxx = getCurrentLocation(x, addCurrentLocation) + box.x2
    val bbyy = getCurrentLocation(y, addCurrentLocation) + box.y2
    val bbzz = getCurrentLocation(z, addCurrentLocation) + box.z2
    rotation match {
      case 0 => AxisAlignedBB.getAABBPool.getAABB(bbx, bby, bbz, bbxx, bbyy, bbzz)
      case 3 => AxisAlignedBB.getAABBPool.getAABB(bbz, bby, 1 - bbxx, bbzz, bbyy, 1 - bbx)
      case 2 => AxisAlignedBB.getAABBPool.getAABB(1 - bbxx, bby, 1 - bbzz, 1 - bbx, bbyy, 1 - bbz)
      case 1 => AxisAlignedBB.getAABBPool.getAABB(1 - bbzz, bby, bbx, 1 - bbz, bbyy, bbxx)
      case _ => AxisAlignedBB.getAABBPool.getAABB(bbx - 1, bby, bbz - 1, bbxx + 1, bbyy, bbzz + 1)
    }
  }

  def registerWinterBlock(block: Block) { winterBlocks += block }
}
