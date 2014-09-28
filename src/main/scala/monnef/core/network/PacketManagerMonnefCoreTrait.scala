/*
 * Copyright (c) 2014 monnef.
 */

/*
 * Copyright (c) 2014 monnef.
 */

package monnef.core.network

import java.net.ProtocolException
import monnef.core.Reference
import monnef.core.network.common.PacketIdMap

trait PacketManagerMonnefCoreTrait extends PacketManager with PacketIdMap {
  override type PACKET = PacketMonnefCoreTrait

  final val CHANNEL = Reference.CHANNEL

  def channelFor(packet: PACKET): String = {
    if (!containsPacketClass(packet.getClass)) throw new RuntimeException(s"Cannot find class mapping of packet ${packet.getClass.getName}")
    CHANNEL
  }

  def constructPacket(packetId: Int): PACKET = {
    val c = packetClass(packetId)
    if (c == null) throw new ProtocolException("Unknown Packet Id!")
    else c.newInstance()
  }

  def packetHandler: CorePacketHandlerTrait
}

class PacketManagerMonnefCoreMC17(handler: CorePacketHandlerMC17) extends PacketManagerMonnefCoreTrait {
  override def packetHandler: CorePacketHandlerTrait = handler
}
