package monnef.core.network.message

trait MessageIn[IN] {
  def readByte(): Byte

  def readInt(): Int

  def readString(): String

  def readChar(): Char

  def readBoolean(): Boolean

  def readDouble(): Double

  def get: IN
}
