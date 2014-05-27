package monnef.core.network

import monnef.core.mod.MonnefCoreNormalMod
import io.netty.handler.codec.MessageToMessageCodec
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import monnef.core.network.common.PacketMonnefCoreBase
import io.netty.channel.{ChannelHandlerContext, ChannelHandler}
import monnef.core.network.Message.{MessageInMC17}
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.network.{NetHandlerPlayServer, INetHandler}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.relauncher.{SideOnly, Side}
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

  override def encode(ctx: ChannelHandlerContext, msg: PacketMonnefCoreBase, out: util.List[AnyRef]) {
    // TODO
  }
}
