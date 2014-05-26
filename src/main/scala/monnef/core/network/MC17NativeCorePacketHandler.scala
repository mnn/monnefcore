package monnef.core.network

import monnef.core.mod.MonnefCoreNormalMod
import io.netty.handler.codec.MessageToMessageCodec
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import monnef.core.network.common.PacketMonnefCoreBase
import io.netty.channel.{ChannelHandlerContext, ChannelHandler}
import monnef.core.network.Message.{MessageInMC17, MessageObjectMC17}
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.network.{NetHandlerPlayServer, INetHandler}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.Minecraft

@ChannelHandler.Sharable
class MC17NativeCorePacketHandler extends MessageToMessageCodec[FMLProxyPacket, PacketMonnefCoreBase] {

  def decode(ctx: ChannelHandlerContext, msg: FMLProxyPacket, out: java.util.List[Object]) {
    val in = (MessageObjectMC17.instance createIn msg).asInstanceOf[MessageInMC17] // hack
    var player: EntityPlayer = null
    FMLCommonHandler.instance.getEffectiveSide match {
      case CLIENT =>
        player = this.getClientPlayer

      case SERVER =>
        val netHandler: INetHandler = ctx.channel.attr(NetworkRegistry.NET_HANDLER).get
        player = netHandler.asInstanceOf[NetHandlerPlayServer].playerEntity

      case _ =>
    }
    MonnefCoreNormalMod.packetHandler.asInstanceOf[CorePacketHandlerMC17].onPacket(in, player)
    out.add(in.resultPacket)
  }

  @SideOnly(Side.CLIENT) private def getClientPlayer: EntityPlayer = Minecraft.getMinecraft.thePlayer
}
