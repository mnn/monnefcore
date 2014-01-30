/*
 * Copyright (c) 2013 monnef.
 */
package monnef.core.network

import cpw.mods.fml.common.network.PacketDispatcher
import cpw.mods.fml.common.network.Player
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.packet.Packet

object NetworkHelper {
  def sendToServer(packet: Packet) {
    PacketDispatcher.sendPacketToServer(packet)
  }

  def sendToAllAround(entity: Entity, range: Double, packet: Packet) {
    sendToAllAround(entity.posX, entity.posY, entity.posZ, entity.dimension, range, packet)
  }

  def sendToAllAround(x: Double, y: Double, z: Double, dim: Int, range: Double, packet: Packet) {
    PacketDispatcher.sendPacketToAllAround(x, y, z, range, dim, packet)
  }

  def sendToClient(packet: Packet, player: EntityPlayer) {
    PacketDispatcher.sendPacketToPlayer(packet, player.asInstanceOf[Player])
  }
}