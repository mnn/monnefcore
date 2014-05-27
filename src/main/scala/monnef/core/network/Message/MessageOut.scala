package monnef.core.network.message

trait MessageOut[OUT] {
  def writeByte(d: Byte): Unit

  def writeInt(d: Int): Unit

  def writeString(d: String): Unit

  def writeChar(d: Char): Unit

  def get: OUT

  def toByteArray: Array[Byte]
}
