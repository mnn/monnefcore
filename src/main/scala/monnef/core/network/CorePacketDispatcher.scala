/*
 * Copyright (c) 2014 monnef.
 */

package monnef.core.network

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer

trait CorePacketDispatcher {
  type PACKET <: PacketMonnefCoreTrait

  def sendToServer(packet: PACKET)

  def sendToAllAround(entity: Entity, range: Double, packet: PACKET)

  def sendToAllAround(x: Double, y: Double, z: Double, dim: Int, range: Double, packet: PACKET)

  def sendToClient(packet: PACKET, player: EntityPlayer)

  def sendToDimension(packet: PACKET, dim: Int)
}

