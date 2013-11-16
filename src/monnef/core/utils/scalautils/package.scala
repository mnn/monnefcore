/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.utils

import scala.collection.IterableLike

package object scalautils {

  implicit class PipedObject[A](value: A) {
    def |>[B](f: A => B): B = f(value)
  }

  implicit class PipedObjectTuple1[A](value: Tuple1[A]) {
    def |>[B](f: (A) => B): B = f(value._1)
  }

  implicit class PipedObjectTuple2[A, B](value: (A, B)) {
    def |>[O](f: (A, B) => O): O = f(value._1, value._2)
  }

  implicit class PipedObjectTuple3[A, B, C](value: (A, B, C)) {
    def |>[O](f: (A, B, C) => O): O = f(value._1, value._2, value._3)
  }

  implicit class PipedObjectTuple4[A, B, C, D](value: (A, B, C, D)) {
    def |>[O](f: (A, B, C, D) => O): O = f(value._1, value._2, value._3, value._4)
  }

  implicit class ListPimps[T](l: List[T]) {
    def shiftTrivial(v: Int) = {
      if (v < 0 || v > l.size) throw new IllegalArgumentException
      l.takeRight(l.size - v) ::: l.take(v)
    }
  }

  implicit class StringPimps(s: String) {
    def toIntOpt: Option[Int] = {
      try {
        Some(s.toInt)
      } catch {
        case e: Exception => None
      }
    }

    def makeFirstCapital: String = {
      val (first, rest) = s.splitAt(1)
      first.toUpperCase + rest
    }

    def insertSpaceOnLowerUpperCaseChange: String = {
      s.replaceAll("(\\p{javaLowerCase})(\\p{javaUpperCase})", "$1 $2")
    }
  }

  lazy val centeredSquares = CenteredSquareGenerator.centeredSquares(0)

  object CenteredSquareGenerator {
    type POS2D = (Int, Int)
    type BOARD2D = List[POS2D]

    def centeredSquares(rad: Int): Stream[BOARD2D] =
      generateOneSquare(rad) #::
        centeredSquares(rad + 1)

    def generateOneSquare(rad: Int): BOARD2D = {
      if (rad <= 0) {
        List((0, 0))
      } else {
        // "half"
        val largerHalf: BOARD2D = (for {
          x <- -rad to rad
          y <- List(rad, -rad)
        } yield (x, y)).toList
        val smallerHalf = largerHalf.filter {case (x, y) => x != y && x != -y}.map {case (x, y) => (y, x)}
        largerHalf ++ smallerHalf
      }
    }
  }

  implicit class IterableLikePimps[A, Repr](s: IterableLike[A, Repr]) {
    def zipWithIndexFirst[A1 >: A, That](implicit bf: scala.collection.generic.CanBuildFrom[Repr, Tuple2[Int, A1], That]): That = {
      var i = -1
      val b = bf(s.repr)
      b.sizeHint(s)
      for {a <- s} {
        i += 1
        b += Tuple2(i, a)
      }
      b.result()
    }
  }

}