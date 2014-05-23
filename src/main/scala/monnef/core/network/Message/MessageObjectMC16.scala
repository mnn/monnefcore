package monnef.core.network.message

import com.google.common.io.{ByteStreams, ByteArrayDataOutput, ByteArrayDataInput}
import net.minecraft.network.packet.Packet250CustomPayload

object MessageObjectMC16 {
  val instance = new MessageObjectMC16
}

class MessageObjectMC16 extends MessageObject {
  type IN = ByteArrayDataInput
  type OUT = ByteArrayDataOutput
  type RAW_PACKET = Packet250CustomPayload

  def createIn(packet: RAW_PACKET): MessageIn[IN] = new MessageInMC16(ByteStreams.newDataInput(packet.data))

  def createOut: MessageOut[OUT] = new MessageOutMC16()
}

class MessageInMC16(input: ByteArrayDataInput) extends MessageIn[ByteArrayDataInput] {
  def readByte(): Byte = input.readByte()

  def readInt(): Int = input.readInt()

  def readString(): String = input.readUTF()

  def readChar(): Char = input.readChar()

  def get: ByteArrayDataInput = input
}

class MessageOutMC16(output: ByteArrayDataOutput = ByteStreams.newDataOutput()) extends MessageOut[ByteArrayDataOutput] {
  def writeByte(d: Byte): Unit = output.writeByte(d)

  def writeInt(d: Int): Unit = output.writeInt(d)

  def writeString(d: String): Unit = output.writeUTF(d)

  def writeChar(d: Char): Unit = output.writeChar(d)

  def get: ByteArrayDataOutput = output

  def toByteArray: Array[Byte] = output.toByteArray
}