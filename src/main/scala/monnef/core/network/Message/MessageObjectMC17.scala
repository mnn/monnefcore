package monnef.core.network.Message

import monnef.core.network.message._
import com.google.common.io.ByteArrayDataOutput
import io.netty.buffer.{Unpooled, ByteBuf}
import monnef.core.network.common.PacketMonnefCoreBase

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