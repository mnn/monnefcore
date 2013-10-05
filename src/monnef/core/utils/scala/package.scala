/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.utils

package object scala {

  implicit class PipedObject[A](value: A) {
    def |>[B](f: A => B): B = f(value)
  }

}
