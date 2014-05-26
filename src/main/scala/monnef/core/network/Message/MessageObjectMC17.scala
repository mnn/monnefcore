package monnef.core.network.Message

import monnef.core.network.message._
import com.google.common.io.{ByteStreams, ByteArrayDataOutput, ByteArrayDataInput}
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import io.netty.buffer.{Unpooled, ByteBuf}
import monnef.core.network.common.PacketMonnefCoreBase

object MessageObjectMC17 {
  val instance = new MessageObjectMC17
}

class MessageObjectMC17 extends MessageObject {
  type IN = ByteBuf
  type OUT = ByteBuf
  type RAW_PACKET = FMLProxyPacket

  def createIn(packet: RAW_PACKET): MessageIn[IN] = new MessageInMC17(ByteStreams.newDataInput(packet.data))

  def createOut: MessageOut[OUT] = new MessageOutMC17()
}

class MessageInMC17(input: ByteBuf) extends MessageIn[ByteBuf] {
  def readByte(): Byte = input.readByte()

  def readInt(): Int = input.readInt()

  def readString(): String = input.readUTF()

  def readChar(): Char = input.readChar()

  def get: ByteBuf = input

  var resultPacket: PacketMonnefCoreBase = _
}

class MessageOutMC17(output: ByteBuf = Unpooled.buffer()) extends MessageOut[ByteBuf] {
  def writeByte(d: Byte): Unit = output.writeByte(d)

  def writeInt(d: Int): Unit = output.writeInt(d)

  def writeString(d: String): Unit = output.writeUTF(d)

  def writeChar(d: Char): Unit = output.writeChar(d)

  def get: ByteArrayDataOutput = output

  def toByteArray: Array[Byte] = output.toByteArray
}