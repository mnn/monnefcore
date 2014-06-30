package monnef.core.network

import monnef.core.network.message.MessageOut

/*
 * Copyright (c) 2014 monnef.
 */

trait PacketManager {
  type PACKET = PacketMonnefCoreTrait

  def writePacketId(packet: PACKET, output: MessageOut[_]) {
    output.writeByte(packetId(packet.getClass).asInstanceOf[Byte])
  }

  def packetId(packet: Class[_ <: PACKET]): Int

  def channelFor(packet: PACKET): String

  def constructPacket(packetId: Int): PACKET

  def packetHandler: CorePacketHandlerTrait

  def registerPacket(id: Int, packetClass: Class[_ <: PACKET])
}
