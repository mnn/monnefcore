package monnef.core.client

import monnef.core.{MonnefCorePlugin, Reference}
import java.util.{Date, UUID}
import monnef.core.mod.MonnefCoreNormalMod
import net.minecraftforge.common.config.Configuration
import com.google.common.io.BaseEncoding
import monnef.core.utils.{scalautils, WebHelper}
import scala.collection.JavaConverters._
import scala.collection.mutable
import scalautils._

class SashRegistry {

  import SashRegistry._

  var FORCE_LOCAL_SASH = false
  var db = Map[UUID, SashRecord]()
  val dummySashRecord = Some(SashRecord(UUID.randomUUID(), 1, None))

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

  def processSashLinesFromWeb(lines: mutable.Buffer[String]): Boolean = {
    val chopped = lines.map {_.split(" ").toSeq}
    if (!chopped.forall(_.size == 3)) false
    else {
      // nick, uuid, sash number
      val triples = chopped.map { a => (a(0), a(1), a(2))}
      val convertedOpt: Seq[Option[(String, UUID, Int)]] = triples.map {
        case (nickStr, uuidStr, sashNumStr) =>
          val sashNum = sashNumStr.toIntOpt
          val uuid = uuidStr.toUuidOpt
          if (sashNum.nonEmpty && uuid.nonEmpty) Some((nickStr, uuid.get, sashNum.get))
          else None
      }
      if (convertedOpt.exists(_.isEmpty)) false
      else {
        db = convertedOpt.map {
          case Some((nick, uuid, sashNumber)) =>
            val newItem = SashRecord(uuid, sashNumber, Some(nick))
            uuid -> newItem
        }.toMap
        MonnefCorePlugin.Log.printFine(s"Obtained sash data: ${formatDb()}.")
        true
      }
    }
  }

  def refreshFromWeb(): Boolean = {
    val javaLines = new java.util.ArrayList[String]()
    if (WebHelper.getLinesTillFooter(SASH_URL, javaLines)) {
      val lines = javaLines.asScala
      if (lines.size == 0) {
        System.err.println(s"Empty sash info.")
        return false
      }
      if (processSashLinesFromWeb(lines)) {
        saveToConfig()
        true
      }
      else false
    } else {
      System.err.println(s"Unable to obtain sash info from web.")
      false
    }
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
    MonnefCoreNormalMod.config.save()
  }

  def init() {
    if (!loadFromConfig()) refreshFromWeb()
  }

  def formatDb(): String = {
    db.values.mkString(", ")
  }
}

object SashRegistry {
  var DEBUG_FORCE_SASH = false
  final val SASH_URL = Reference.URL_JAFFAS + "/sash.txt"
  final val DAY_IN_MILLIS = 24 * 60 * 60 * 1000
  final val SASH_COUNT = 1

  case class SashRecord(uuid: UUID, number: Int, nick: Option[String])

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
    Some(parsedData.map { case (uuid, stype) => (uuid, SashRecord(uuid, stype, None))}.toMap)
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
