/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils

import monnef.core.api.IIntegerCoordinates
import java.text.DecimalFormat
import net.minecraft.util.MathHelper.ceiling_float_int
import net.minecraft.util.MathHelper.sqrt_float

object MathHelper {
  def square(number: Int): Int = number * number

  def exactDistance(a: IIntegerCoordinates, b: IIntegerCoordinates): Float = sqrt_float(square(a.getX - b.getX) + square(a.getY - b.getY) + square(a.getZ - b.getZ))

  def exactDistanceInt(a: IIntegerCoordinates, b: IIntegerCoordinates): Int = ceiling_float_int(exactDistance(a, b))

  def degToRad(deg: Float): Float = (deg * Math.PI / 180f).asInstanceOf[Float]

  def range(start: Int, count: Int): Array[Int] = range(start, count, 1)

  def range(start: Int, count: Int, step: Int): Array[Int] = (for {j <- 0 until count} yield start + j * step).toArray

  def scaleValue(value: Int, valueMax: Int, newScaleMax: Int): Int = (value * newScaleMax) / valueMax

  final val oneDecimalPlace: DecimalFormat = new DecimalFormat("#.#")

  def getIntSquareRootJava(in: Int): Option[Integer] = IntSquareRoot.getRoot(in).map(a => a.asInstanceOf[Integer])

  object IntSquareRoot {
    private var squareToRoot: Map[Int, Int] = _
    private var biggestSquare: Int = _

    // init
    purgeCache()

    private[this] def generateNextPair() {
      val currRoot = squareToRoot(biggestSquare)
      val newRoot = currRoot + 1
      val newSquare = newRoot * newRoot
      squareToRoot += newSquare -> newRoot
      biggestSquare = newSquare
    }

    def getRoot(in: Int): Option[Int] = {
      if (in > biggestSquare) while (biggestSquare < in) generateNextPair()
      squareToRoot.get(in)
    }

    def purgeCache() {
      squareToRoot = Map(0 -> 0, 1 -> 1)
      biggestSquare = 1
    }
  }

}