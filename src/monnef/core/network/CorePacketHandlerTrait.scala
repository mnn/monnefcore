package monnef.core.network

import monnef.core.network.message.MessageIn
import cpw.mods.fml.common.network.Player

trait CorePacketHandlerTrait extends MessageObjectTypeTrait {
  def onLoad() {}

  def onPostLoad() {}

  def onPacket(in: MessageIn[MESSAGE_OBJ#IN], player: Player)

  def dispatcher: CorePacketDispatcher

  def manager: PacketManager
}
