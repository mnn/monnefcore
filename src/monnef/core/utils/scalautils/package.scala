/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.utils

package object scalautils {

  implicit class PipedObject[A](value: A) {
    def |>[B](f: A => B): B = f(value)
  }

  implicit class PipedObjectTuple1[A](value: (A)) {
    def |>[B](f: (A) => B): B = f(value)
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

  implicit class ListUtils[T](l: List[T]) {
    def shiftTrivial(v: Int) = {
      if (v < 0 || v > l.size) throw new IllegalArgumentException
      l.takeRight(l.size - v) ::: l.take(v)
    }
  }
}
