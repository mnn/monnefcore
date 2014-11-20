package monnef.core.test

import org.junit._
import org.junit.Assert._
import monnef.core.utils.RandomHelper

class RandomHelperTest {
  val iters = 10000
  val percent = iters / 100

  @Test def oneWightedSmallEnd() {
    val data = Seq('a' -> 99, 'b' -> 1)
    var bees = 0
    for (i <- 0 until iters) if (RandomHelper.randomlySelectOne(data) == 'b') bees += 1
    assertTrue(bees <= 2 * percent)
    assertNotEquals(bees, 0)
    println(s"oneWightedSmallEnd got $bees bees.")
  }

  @Test def oneWightedBigEnd() {
    val data = Seq('a' -> 1, 'b' -> 99)
    var bees = 0
    for (i <- 0 until iters) if (RandomHelper.randomlySelectOne(data) == 'b') bees += 1
    assertTrue(bees > 2 * percent)
    assertNotEquals(bees, iters)
    println(s"oneWightedBigEnd got $bees bees.")
  }

  @Test def oneWightedZeroEnd() {
    val data = Seq('a' -> 100, 'b' -> 0)
    var bees = 0
    for (i <- 0 until iters) if (RandomHelper.randomlySelectOne(data) == 'b') bees += 1
    assertEquals(0, bees)
    println(s"oneWightedZeroEnd got $bees bees.")
  }

  @Test def oneWightedZeroStart() {
    val data = Seq('a' -> 0, 'b' -> 100)
    var bees = 0
    for (i <- 0 until iters) if (RandomHelper.randomlySelectOne(data) == 'b') bees += 1
    assertEquals(iters, bees)
    println(s"oneWightedZeroStart got $bees bees.")
  }

  @Test def oneWightedZeroMid() {
    val data = Seq('x' -> 100, 'b' -> 0, 'a' -> 100)
    var bees = 0
    for (i <- 0 until iters) if (RandomHelper.randomlySelectOne(data) == 'b') bees += 1
    assertEquals(0, bees)
    println(s"oneWightedZeroMid got $bees bees.")
  }

  @Test def oneWightedZeroEdges() {
    val data = Seq('x' -> 0, 'b' -> 100, 'a' -> 0)
    var bees = 0
    for (i <- 0 until iters) if (RandomHelper.randomlySelectOne(data) == 'b') bees += 1
    assertEquals(iters, bees)
    println(s"oneWightedZeroEdges got $bees bees.")
  }
}
