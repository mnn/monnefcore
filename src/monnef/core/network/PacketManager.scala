package monnef.core.network

trait PacketManager extends PacketTypes {
  def packetId(packet: Class[_ <: PACKET]): Int

  def channelFor(packet: PACKET): String

  def constructPacket(packetId: Int): PACKET

  def packetHandler: CorePacketHandlerTrait

  def registerPacket(id: Int, packetClass: Class[_ <: PACKET])
}

