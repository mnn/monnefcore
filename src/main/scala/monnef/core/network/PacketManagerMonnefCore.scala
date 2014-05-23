/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.network

import com.google.common.collect.{HashBiMap, ImmutableBiMap, BiMap}
import java.net.ProtocolException
import monnef.core.Reference

trait PacketManagerMonnefCoreTrait extends PacketManager with PacketIdMap {
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

/*
object PacketManagerMonnefCore extends PacketManagerMonnefCoreTrait {
}
*/

trait PacketManagerMonnefCoreTraitMC16 extends PacketManagerMonnefCoreTrait with PacketTypeMC16 {
}

object PacketManagerMonnefCoreMC16 extends PacketManagerMonnefCoreTraitMC16 {
  var packetHandler: CorePacketHandlerMC16 = _
}

trait PacketTypes {
  type PACKET <: PacketMonnefCoreTrait
}

trait PacketTypeMC16 extends PacketTypes {
  type PACKET = PacketMonnefCoreMC16
}

trait PacketIdMap extends PacketTypes {

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
