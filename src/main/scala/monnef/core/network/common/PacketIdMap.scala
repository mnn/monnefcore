package monnef.core.network.common

trait PacketIdMap {
  type PACKET

  /*
   0  - 31 ... jaffas
   32 - 63 ... crafting
   */

  // because Scala cannot process HashBiMap class file (bad constant pool index: 0 at pos: 8582),
  // here is my lame custom "HashBiMap"

  private var idToPacket: Map[Int, Class[_ <: PACKET]] = Map.empty
  private var packetToId: Map[Class[_ <: PACKET], Int] = Map.empty

  def containsId(packetId: Int) = idToPacket.contains(packetId)

  def containsPacketClass(packetClass: Class[_ <: PACKET]) = packetToId.contains(packetClass)

  def packetId(packet: Class[_ <: PACKET]): Int = packetToId(packet)

  def packetClass(packetId: Int): Class[_ <: PACKET] = idToPacket(packetId)

  def registerPacket(id: Int, packetClass: Class[_ <: PACKET]) {
    if (containsId(id)) throw new RuntimeException("Packet id already registered.")
    idToPacket += id -> packetClass
    packetToId += packetClass -> id
  }
}
