package monnef.core.client

import monnef.core.{MonnefCorePlugin, Reference}
import java.util.{Date, UUID}
import monnef.core.mod.MonnefCoreNormalMod
import net.minecraftforge.common.config.Configuration
import com.google.common.io.BaseEncoding

class SashRegistry {

  import SashRegistry._

  var FORCE_LOCAL_SASH = false
  var db = Map[UUID, SashRecord]()
  val dummySashRecord = Some(SashRecord(UUID.randomUUID(), 1))

  private def configProperty = MonnefCoreNormalMod.config.get(Configuration.CATEGORY_GENERAL, "sashCache", "")

  def getSashRecord(uuid: UUID): Option[SashRecord] = {
    val r = db.get(uuid)
    if (r.isEmpty && FORCE_LOCAL_SASH) dummySashRecord
    else r
  }

  def getSashNumber(uuid: UUID): Int =
    getSashRecord(uuid) match {
      case Some(sr) => sr.number
      case None => 0
    }

  def refreshFromWeb() {
    // TODO
  }

  def loadFromConfig(): Boolean = {
    val data = configProperty.getString
    if (data == "") false
    else deserialize(data) match {
      case Some(newDb) => db = newDb; true
      case None => false
    }
  }

  def saveToConfig() {
    configProperty.set(serialize(db))
  }
}

object SashRegistry {
  var DEBUG_FORCE_SASH = false
  val SASH_URL = Reference.URL_JAFFAS + "/sash.txt"
  val DAY_IN_MILLIS = 24 * 60 * 60 * 1000

  case class SashRecord(uuid: UUID, number: Int)

  def deserialize(data: String): Option[Map[UUID, SashRecord]] = {
    val dec: String = new String(BaseEncoding.base64Url().decode(data))
    log(s"Decoded sash data: '$dec'")
    val parts = dec.split(" ").toSeq
    if (parts.size < 1 || !parts.head.forall(_.isDigit)) return None
    val lastUpdated = new Date(parts.head.toLong)
    val currentTime = new Date
    val validUntil = new Date(lastUpdated.getTime + DAY_IN_MILLIS)
    if (lastUpdated.after(currentTime)) return None
    if (validUntil.before(currentTime)) return None
    deserializeDb(parts.tail)
  }

  def deserializeDb(partsWithUsers: Seq[String]): Option[Map[UUID, SashRecord]] = {
    if (partsWithUsers.size % 2 != 0) {
      MonnefCorePlugin.Log.printWarning(s"Size of user parts not dividable be 2.")
      return None
    }
    var parsedData: Seq[(UUID, Int)] = Seq()
    try {
      parsedData = partsWithUsers.grouped(2).map { s => (s(0), s(1))}.map { case (uuidString, sashType) =>
        (UUID.fromString(uuidString), sashType.toInt)
      }.toSeq
    } catch {
      case t: Throwable =>
        MonnefCorePlugin.Log.printWarning(s"Cannot parse item.")
        t.printStackTrace()
        return None
    }
    Some(parsedData.map { case (uuid, stype) => (uuid, SashRecord(uuid, stype))}.toMap)
  }

  def serialize(db: Map[UUID, SashRecord]): String = {
    val text = new Date().getTime + " " + serializeDb(db)
    log(s"Generated text for config: $text")
    BaseEncoding.base64Url().encode(text.getBytes)
  }

  def serializeDb(db: Map[UUID, SashRecord]): String = db.map(i => i._1 + " " + i._2.number).mkString(" ")

  def log(msg: String) {
    MonnefCorePlugin.Log.printFine(s"[SashHandler] $msg")
  }
}
