package monnef.core.network

import monnef.core.network.message.MessageIn
import net.minecraft.entity.player.EntityPlayer

trait CorePacketHandlerTrait {
  def onLoad() {}

  def onPostLoad() {}

  def onPacket(in: MessageIn[_], player: EntityPlayer)

  def dispatcher: CorePacketDispatcher

  def manager: PacketManager
}
