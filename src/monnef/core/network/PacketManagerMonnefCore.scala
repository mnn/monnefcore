/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.network

import com.google.common.collect.{ImmutableBiMap, BiMap}
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
}

object PacketManagerMonnefCore extends PacketManagerMonnefCoreTrait {
}

trait PacketManagerMonnefCoreTraitMC16 extends PacketManagerMonnefCoreTrait with PacketTypeMC16 {
}

object PacketManagerMonnefCoreMC16 extends PacketManagerMonnefCoreTraitMC16 {
}

trait PacketTypes {
  type PACKET <: PacketMonnefCoreTrait
  //type PACKET_CLASS <: Class[_ <: PACKET]
}

trait PacketTypeMC16 extends PacketTypes {
  type PACKET = PacketMonnefCoreMC16
  //type PACKET_CLASS = Class[_ <: PACKET]
}

trait PacketIdMap extends PacketTypes {

  /*
   0  - 31 ... jaffas
   32 - 63 ... crafting
   */

  private val idMap: BiMap[Int, Class[_ <: PACKET]] = {
    val builder = ImmutableBiMap.builder[Int, Class[_ <: PACKET]]()
    onIdMapBuild(builder)
    builder.build()
  }

  // don't forget to call super!
  def onIdMapBuild(builder: ImmutableBiMap.Builder[Int, Class[_ <: PACKET]]) = {}

  def containsId(packetId: Int) = idMap.containsKey(packetId)

  def containsPacketClass(packetClass: Class[_ <: PACKET]) = idMap.containsValue(packetClass)

  def packetId(packet: Class[_ <: PACKET]): Int = idMap.inverse().get(packet)

  def packetClass(packetId: Int): Class[_ <: PACKET] = idMap.get(packetId)
}
