/*
 * Copyright (c) 2014 monnef.
 */

package monnef.core.network.message

import io.netty.buffer.{Unpooled, ByteBuf}
import monnef.core.network.common.PacketMonnefCoreBase

class MessageInMC17(input: ByteBuf) extends MessageIn[ByteBuf] {
  override def readByte(): Byte = input.readByte()

  override def readInt(): Int = input.readInt()

  override def readString(): String = {
    val len = input.readInt()
    val buff = new Array[Byte](len)
    input.readBytes(buff)
    new String(buff, "UTF-8")
  }

  override def readChar(): Char = input.readChar()

  override def readBoolean(): Boolean = input.readBoolean()

  override def get: ByteBuf = input

  var resultPacket: PacketMonnefCoreBase = _
}

class MessageOutMC17(output: ByteBuf = Unpooled.buffer()) extends MessageOut[ByteBuf] {
  override def writeByte(d: Byte): Unit = output.writeByte(d)

  override def writeInt(d: Int): Unit = output.writeInt(d)

  override def writeString(d: String) {
    output.writeInt(d.length)
    output.writeBytes(d.getBytes("UTF-8"))
  }

  override def writeChar(d: Char): Unit = output.writeChar(d)

  override def writeBoolean(d: Boolean): Unit = output.writeBoolean(d)

  override def get: ByteBuf = output
}