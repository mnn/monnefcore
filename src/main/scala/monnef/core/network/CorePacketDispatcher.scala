/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.network

import cpw.mods.fml.common.network.{Player, PacketDispatcher}
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer

trait CorePacketDispatcher extends MessageObjectTypeTrait with PacketTypes {
  def sendToServer(packet: MESSAGE_OBJ#RAW_PACKET)

  def packAndSendToServer(packet: PACKET) //{ sendToServer(packet.makePacket()) }

  def sendToAllAround(entity: Entity, range: Double, packet: MESSAGE_OBJ#RAW_PACKET)

  def sendToAllAround(x: Double, y: Double, z: Double, dim: Int, range: Double, packet: MESSAGE_OBJ#RAW_PACKET)

  def sendToClient(packet: MESSAGE_OBJ#RAW_PACKET, player: EntityPlayer)

  def packAndSendToClient(packet: PACKET, player: EntityPlayer)
}

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
