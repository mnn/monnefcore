package monnef.core.test

import org.junit._
import org.junit.Assert._
import monnef.core.client.SashRegistry
import java.util.UUID
import monnef.core.client.SashRegistry.SashRecord

class SashRegistryTest {

  def createTestingDbSimple = {
    val u1 = UUID.randomUUID()
    val p1 = SashRecord(u1, 0, None)
    val u2 = UUID.randomUUID()
    val p2 = SashRecord(u2, 1, None)
    Map(u1 -> p1, u2 -> p2)
  }

  @Test def serializationDb() {
    val sr = SashRegistry
    val db1 = createTestingDbSimple
    val data = sr.serializeDb(db1)
    println(s"serialized db: $data")
    val db1d = sr.deserializeDb(data.split(" "))
    assertEquals(Some(db1), db1d)
  }

  @Test def serialization() {
    val sr = SashRegistry
    val db1 = createTestingDbSimple
    val data = sr.serialize(db1)
    println(s"serialized: $data")
    val db1d = sr.deserialize(data)
    assertEquals(Some(db1), db1d)
  }
}
