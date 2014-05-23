/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.test

import org.junit._
import Assert._
import monnef.core.utils.scalautils._
import scala.reflect.runtime.universe._

class ScalaUtilsTest {

  trait Inc {
    def inc()
  }

  class Foo extends Inc {
    var value = 0

    def inc() { value += 1 }
  }

  @Test def tryAsInstanceOf01() {
    val foo = new Foo
    foo.tryAsInstanceOf((a: Inc) => a.inc())
    assertEquals(1, foo.value)
  }

  @Test def tryAsInstanceOf02() {
    val t = new C2 with B2
    assert(t.isInstanceOf[C2])
    assert(t.isInstanceOf[B2])
    assert(t.isInstanceOfCustom[C2])
    assert(t.isInstanceOfCustom[B2])
    assert(t.isInstanceOfCustom[A2])
    t.lateInit()
    assertEquals(1, t.value)
  }

  implicit class TestingPimp01[IN: TypeTag](in: IN) {
    def isInst[OUT: TypeTag]: Boolean = typeOf[IN] <:< typeOf[OUT]

    def ifInstApply[OUT: TypeTag](f: OUT => Unit) { if (in.isInst[OUT]) f(in.asInstanceOf[OUT]) }
  }

  @Test def tryAsInstanceOf03() {
    assert((new C2 with B2).isInst[C2])
    assert((new C2 with B2).isInst[B2])
    assert((new C2 with B2).isInst[A2])

    val x = new C2 with B2
    assertEquals(0, x.value)
    x.ifInstApply((a: String) => a.size)
    x.ifInstApply((a: B2) => a.i)
    assertEquals(1, x.value)
  }
}

trait A2 {
  println("A2")
}

trait B2 extends A2 {
  println("B2")

  var value = {
    println("0")
    0
  }

  def i {
    println("1")
    value = 1
  }
}

class C2 {
  println("C2")

  def lateInit() {
    println("li")

    if (this.isInstanceOf[B2]) println("isInOf " + this.asInstanceOf[B2].value)
    if (classOf[B2].isAssignableFrom(this.getClass)) println("isAssFr " + this.asInstanceOf[B2].value)

    this.tryAsInstanceOf((a: B2) => {
      println("annon func")
      a.i
    }: Unit)
  }
}
