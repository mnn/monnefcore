/*
 * Copyright (c) 2014 monnef.
 */

package monnef.core.network

import monnef.core.network.message.{MessageObjectMC16, MessageObject, MessageIn}
import net.minecraft.entity.player.{EntityPlayerMP, EntityPlayer}
import cpw.mods.fml.relauncher.Side
import java.net.ProtocolException
import monnef.core.{Reference, MonnefCorePlugin}
import cpw.mods.fml.common.network.{NetworkRegistry, FMLEmbeddedChannel}
import java.util
import monnef.core.utils.PlayerHelper

abstract class CorePacketHandlerBase extends CorePacketHandlerTrait {
  def onPacket(in: MessageIn[_], player: EntityPlayer) {
    try {
      val packetId = in.readByte()
      val corePacket = manager.constructPacket(packetId)
      corePacket.read(in)
      corePacket.execute(player, if (player.worldObj.isRemote) Side.CLIENT else Side.SERVER)
    } catch {
      case e: ProtocolException =>
        player match {
          case p: EntityPlayerMP =>
            p.playerNetServerHandler.kickPlayerFromServer("Protocol Exception!")
            MonnefCorePlugin.Log.printWarning(s"Player ${PlayerHelper.formatPlayerID(p)} caused a Protocol Exception!")
          case p: EntityPlayer =>
            MonnefCorePlugin.Log.printWarning(s"Unknown player object ${PlayerHelper.formatPlayerID(p)} caused a Protocol Exception! $p")
          case _ =>
            MonnefCorePlugin.Log.printWarning(s"Unknown non-player object caused a Protocol Exception! $player")
        }
      case e: IllegalAccessException =>
        throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e)
      case e: InstantiationException =>
        throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e)
    }
  }
}

class CorePacketHandlerMC17 extends CorePacketHandlerBase {
  private var channels: util.EnumMap[Side, FMLEmbeddedChannel] = _
  private var nativeHandler: MC17NativeCorePacketHandler = _
  private var _manager: PacketManagerMonnefCoreMC17 = _
  private var _dispatcher: CorePacketDispatcherMC17 = _

  override def onLoad() {
    super.onLoad()
    _dispatcher = new CorePacketDispatcherMC17(this)
    _manager = new PacketManagerMonnefCoreMC17(this)
    nativeHandler = new MC17NativeCorePacketHandler(this)
    channels = NetworkRegistry.INSTANCE.newChannel(Reference.CHANNEL, nativeHandler)
  }

  override def dispatcher: CorePacketDispatcher = _dispatcher

  override def manager: PacketManager = _manager

  def channelClient = channels.get(Side.CLIENT)

  def channelServer = channels.get(Side.SERVER)
}