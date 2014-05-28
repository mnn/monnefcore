package monnef.core.network

import io.netty.handler.codec.MessageToMessageCodec
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import monnef.core.network.common.PacketMonnefCoreBase
import io.netty.channel.{ChannelHandlerContext, ChannelHandler}
import monnef.core.network.Message.{MessageOutMC17, MessageInMC17}
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.network.{NetHandlerPlayServer, INetHandler}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import cpw.mods.fml.relauncher.Side._
import java.util

@ChannelHandler.Sharable
class MC17NativeCorePacketHandler(parentHandler: CorePacketHandlerMC17) extends MessageToMessageCodec[FMLProxyPacket, PacketMonnefCoreBase] {

  def decode(ctx: ChannelHandlerContext, msg: FMLProxyPacket, out: java.util.List[Object]) {
    val in = new MessageInMC17(msg.payload())
    parentHandler.onPacket(in, getPlayerObject(ctx))
    out.add(in.resultPacket)
  }

  def getPlayerObject(ctx: ChannelHandlerContext): EntityPlayer = FMLCommonHandler.instance.getEffectiveSide match {
    case CLIENT =>
      this.getClientPlayer

    case SERVER =>
      val netHandler: INetHandler = ctx.channel.attr(NetworkRegistry.NET_HANDLER).get
      netHandler.asInstanceOf[NetHandlerPlayServer].playerEntity

    case _ => null
  }

  @SideOnly(CLIENT) private def getClientPlayer: EntityPlayer = Minecraft.getMinecraft.thePlayer

  override def encode(ctx: ChannelHandlerContext, msg: PacketMonnefCoreBase, outList: util.List[AnyRef]) {
    val out = new MessageOutMC17()
    parentHandler.manager.writePacketId(msg, out)
    msg.write(out)
    val proxyPacket = new FMLProxyPacket(out.get.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get())
    outList.add(proxyPacket)
  }
}
