package monnef.core.network.message

trait MessageObject {
  type IN
  type OUT
  type RAW_PACKET

  def createIn(packet: RAW_PACKET): MessageIn[IN]

  def createOut: MessageOut[OUT]
}



