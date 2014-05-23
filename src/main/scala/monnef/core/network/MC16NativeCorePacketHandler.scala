package monnef.core.network

import cpw.mods.fml.common.network.{Player, IPacketHandler}
import net.minecraft.network.INetworkManager
import net.minecraft.network.packet.Packet250CustomPayload
import monnef.core.mod.MonnefCoreNormalMod
import monnef.core.network.message.MessageObjectMC16

class MC16NativeCorePacketHandler extends IPacketHandler {
  def onPacketData(netManager: INetworkManager, rawPacket: Packet250CustomPayload, player: Player) {
    val in = MessageObjectMC16.instance createIn rawPacket
    MonnefCoreNormalMod.packetHandler.asInstanceOf[CorePacketHandlerMC16].onPacket(in, player)
  }
}
