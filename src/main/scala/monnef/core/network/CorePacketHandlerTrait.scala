package monnef.core.network

import monnef.core.network.message.MessageIn
import net.minecraft.entity.player.EntityPlayer

/*
 * Copyright (c) 2013 monnef.
 */

trait CorePacketHandlerTrait {
  def onPreLoad() {}

  def onPostLoad() {}

  def onPacket(in: MessageIn[_], player: EntityPlayer)

  def dispatcher: CorePacketDispatcher

  def manager: PacketManager
}
