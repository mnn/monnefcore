/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.network

import cpw.mods.fml.common.network.{FMLOutboundHandler, Player, PacketDispatcher}
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint

trait CorePacketDispatcher {
  type PACKET <: PacketMonnefCoreTrait

  def sendToServer(packet: PACKET)

  def sendToAllAround(entity: Entity, range: Double, packet: PACKET)

  def sendToAllAround(x: Double, y: Double, z: Double, dim: Int, range: Double, packet: PACKET)

  def sendToClient(packet: PACKET, player: EntityPlayer)

  def sendToDimension(packet: PACKET, dim: Int)
}

/*
object CorePacketDispatcherMC16 extends CorePacketDispatcher with MessageObjectMC16Trait with PacketTypeMC16 {
  def sendToServer(packet: MESSAGE_OBJ#RAW_PACKET) {
    PacketDispatcher.sendPacketToServer(packet)
  }

  def sendToAllAround(entity: Entity, range: Double, packet: MESSAGE_OBJ#RAW_PACKET) {
    sendToAllAround(entity.posX, entity.posY, entity.posZ, entity.dimension, range, packet)
  }

  def sendToAllAround(x: Double, y: Double, z: Double, dim: Int, range: Double, packet: MESSAGE_OBJ#RAW_PACKET) {
    PacketDispatcher.sendPacketToAllAround(x, y, z, range, dim, packet)
  }

  def sendToClient(packet: MESSAGE_OBJ#RAW_PACKET, player: EntityPlayer) {
    PacketDispatcher.sendPacketToPlayer(packet, player.asInstanceOf[Player])
  }

  def packAndSendToServer(packet: PACKET) { sendToServer(packet.makePacket()) }

  def packAndSendToClient(packet: PACKET, player: EntityPlayer) { sendToClient(packet.makePacket(), player) }
}
*/

class CorePacketDispatcherMC17(private val handler: CorePacketHandlerMC17) extends CorePacketDispatcher {
  type PACKET = PacketMonnefCoreTrait

  override def sendToServer(packet: PACKET) {
    handler.channelClient.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER)
    handler.channelClient.writeAndFlush(packet)
  }

  override def sendToAllAround(entity: Entity, range: Double, packet: PACKET) {
    sendToAllAround(entity.posX, entity.posY, entity.posZ, entity.dimension, range, packet)
  }

  override def sendToAllAround(x: Double, y: Double, z: Double, dim: Int, range: Double, packet: PACKET) {
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT)
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new TargetPoint(dim, x, y, z, range))
    handler.channelServer.writeAndFlush(packet)
  }

  override def sendToClient(packet: PACKET, player: EntityPlayer) {
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER)
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player)
    handler.channelServer.writeAndFlush(packet)
  }

  override def sendToDimension(packet: PACKET, dim: Int) {
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION)
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(Integer.valueOf(dim))
    handler.channelServer.writeAndFlush(packet)
  }
}