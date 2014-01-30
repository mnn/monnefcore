package monnef.core.network

import cpw.mods.fml.relauncher.Side
import net.minecraft.entity.player.EntityPlayer
import cpw.mods.fml.common.network.PacketDispatcher
import monnef.core.network.message.{MessageObjectMC16, MessageObject, MessageIn, MessageOut}

trait PacketMonnefCoreTrait extends MessageObjectTypeTrait {
  def messageObj: MessageObject

  def write(out: MessageOut[_])

  def read(in: MessageIn[_])

  def execute(player: EntityPlayer, side: Side)

  def makePacket(): MESSAGE_OBJ#RAW_PACKET

  def manager: PacketManager
}

abstract class PacketMonnefCoreMC16 extends MessageObjectMC16Trait with PacketMonnefCoreTrait with PacketTypeMC16 {
  val messageObj: MessageObject = new MessageObjectMC16()

  def makePacket(): MESSAGE_OBJ#RAW_PACKET = {
    val out = messageObj.createOut
    out.writeByte(manager.packetId(this.getClass).toByte)
    write(out)
    PacketDispatcher.getPacket(manager.channelFor(this), out.toByteArray)
  }

  def manager = PacketManagerMonnefCoreMC16
}

