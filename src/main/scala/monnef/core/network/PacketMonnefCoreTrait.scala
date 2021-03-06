package monnef.core.network

import cpw.mods.fml.relauncher.Side
import net.minecraft.entity.player.{EntityPlayerMP, EntityPlayer}
import monnef.core.network.message.{MessageIn, MessageOut}
import java.net.ProtocolException

trait PacketMonnefCoreTrait {
  def write(out: MessageOut[_])

  def read(in: MessageIn[_])

  final def execute(player: EntityPlayer, side: Side) {
    try {
      if (side == Side.CLIENT) executeClient(player.asInstanceOf[EntityPlayer])
      else executeServer(player.asInstanceOf[EntityPlayerMP])
    } catch {
      case e: ClassCastException => throw new RuntimeException("Cannot convert player to its side version")
    }
  }

  val WRONG_SIDE = () => throw new ProtocolException("Packet is being processed by wrong/unsupported side.")

  def executeServer(player: EntityPlayerMP): Unit = WRONG_SIDE()

  def executeClient(player: EntityPlayer): Unit = WRONG_SIDE()

  def manager: PacketManager

  def sendToServer()

  def sendToClient(player: EntityPlayer)
}

abstract class PacketMonnefCoreMC17 extends PacketMonnefCoreTrait {
  def dispatcherMC17: CorePacketDispatcherMC17 = manager.packetHandler.dispatcher.asInstanceOf[CorePacketDispatcherMC17]

  override def sendToServer(): Unit = dispatcherMC17.sendToServer(this)

  override def manager: PacketManager = CorePacketHandlerMC17.instance.manager

  override def sendToClient(player: EntityPlayer): Unit = dispatcherMC17.sendToClient(this, player)
}

/*
abstract class PacketMonnefCoreMC16 extends MessageObjectMC16Trait with PacketMonnefCoreTrait with PacketTypeMC16 {
  final val messageObj: MessageObject = new MessageObjectMC16()

  final def makePacket(): MESSAGE_OBJ#RAW_PACKET = {
    val out = messageObj.createOut
    out.writeByte(manager.packetId(this.getClass).toByte)
    write(out)
    PacketDispatcher.getPacket(manager.channelFor(this), out.toByteArray)
  }

  final def manager = PacketManagerMonnefCoreMC16

  private val dispatcher = manager.packetHandler.dispatcher

  final def sendToServer() { dispatcher.sendToServer(this.makePacket()) }

  final def sendToClient(player: EntityPlayer) { dispatcher.sendToClient(this.makePacket(), player) }
}
*/
