/*
 * Copyright (c) 2014 monnef.
 */
package monnef.core.network

import cpw.mods.fml.common.network.FMLOutboundHandler
import net.minecraft.entity.Entity
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint
import net.minecraft.entity.player.EntityPlayer

class CorePacketDispatcherMC17(private val handler: CorePacketHandlerMC17) extends CorePacketDispatcher {
  type PACKET = PacketMonnefCoreTrait

  override def sendToServer(packet: PACKET) {
    handler.channelClient.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER)
    handler.channelClient.writeAndFlush(packet)
  }

  override def sendToAllAround(entity: Entity, range: Double, packet: PACKET) {
    sendToAllAround(entity.posX, entity.posY, entity.posZ, entity.dimension, range, packet)
  }

  override def sendToAllAround(x: Double, y: Double, z: Double, dim: Int, range: Double, packet: PACKET) {
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT)
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new TargetPoint(dim, x, y, z, range))
    handler.channelServer.writeAndFlush(packet)
  }

  override def sendToClient(packet: PACKET, player: EntityPlayer) {
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER)
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player)
    handler.channelServer.writeAndFlush(packet)
  }

  override def sendToDimension(packet: PACKET, dim: Int) {
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION)
    handler.channelServer.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(Integer.valueOf(dim))
    handler.channelServer.writeAndFlush(packet)
  }
}
