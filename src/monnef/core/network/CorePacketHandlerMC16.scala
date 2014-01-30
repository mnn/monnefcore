package monnef.core.network

import monnef.core.network.message.{MessageObjectMC16, MessageObject, MessageIn}
import net.minecraft.entity.player.{EntityPlayerMP, EntityPlayer}
import cpw.mods.fml.relauncher.Side
import java.net.ProtocolException
import monnef.core.MonnefCorePlugin
import cpw.mods.fml.common.network.Player

trait MessageObjectTypeTrait {
  type MESSAGE_OBJ <: MessageObject
}

trait MessageObjectMC16Trait extends MessageObjectTypeTrait {
  override type MESSAGE_OBJ = MessageObjectMC16
}

abstract class CorePacketHandlerBase extends CorePacketHandlerTrait {
  def onPacket(in: MessageIn[MESSAGE_OBJ#IN], player: Player) {
    try {
      val entityPlayer = player.asInstanceOf[EntityPlayer]
      val packetId = in.readByte()
      val corePacket = manager.constructPacket(packetId)
      corePacket.read(in)
      corePacket.execute(entityPlayer, if (entityPlayer.worldObj.isRemote) Side.CLIENT else Side.SERVER)
    } catch {
      case e: ProtocolException =>
        player match {
          case p: EntityPlayerMP =>
            p.playerNetServerHandler.kickPlayerFromServer("Protocol Exception!")
            MonnefCorePlugin.Log.printWarning(s"Player '${p.username}' caused a Protocol Exception!")
          case p: EntityPlayer =>
            MonnefCorePlugin.Log.printWarning(s"Unknown player object '${p.username}' caused a Protocol Exception! $p")
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

class CorePacketHandlerMC16 extends CorePacketHandlerBase with MessageObjectMC16Trait {

  def dispatcher: CorePacketDispatcherMC16.type = CorePacketDispatcherMC16

  def manager: PacketManagerMonnefCoreMC16.type = PacketManagerMonnefCoreMC16
}
