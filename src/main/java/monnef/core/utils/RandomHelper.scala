/*
 * Jaffas and more!
 * author: monnef
 */
package monnef.core.utils

import java.util.Random

object RandomHelper {
  def generateRandomFromInterval(min: Float, max: Float): Float = {
    val size: Float = max - min
    if (size < 0) throw new IllegalArgumentException
    rand.nextFloat * size + min
  }

  def randomPositionInCircle(radius: Float): Array[Float] = {
    val angle: Double = rand.nextDouble
    val x = radius * Math.cos(angle)
    val y = radius * Math.sin(angle)
    Array[Float](x.asInstanceOf[Float], y.asInstanceOf[Float])
  }

  def generateRandomFromSymmetricInterval(len: Float): Float = generateRandomFromInterval(-len, len)

  def generateRandomFromInterval(min: Int, max: Int): Int = {
    val size: Int = max - min
    if (size < 0) throw new IllegalArgumentException
    rand.nextInt(size + 1) + min
  }

  def generateRandomFromSymmetricInterval(len: Int): Int = {
    generateRandomFromInterval(-len, len)
  }

  def rollPercentBooleanDice(percentsOfSuccess: Int): Boolean = {
    rand.nextFloat < percentsOfSuccess / 100f
  }

  def generateRandomFromBaseAndSpread(base: Float, spread: Float): Float = {
    base + generateRandomFromInterval(-spread, spread)
  }

  def generateRandomFromBaseAndSpread(base: Int, spread: Int): Int = {
    base + generateRandomFromInterval(-spread, spread)
  }

  var rand: Random = new Random

  def randomlySelectOne[T](data: Seq[(T, Int)]): T = {
    val fixedData = data.filter(_._2 > 0)
    val totalValue = fixedData.foldLeft(0) { case (acc, (item, value)) => acc + value}
    val randomValue = RandomHelper.rand.nextInt(totalValue)
    var currentValue = 0
    var itemIndex = 0
    while (currentValue < randomValue) {
      currentValue += fixedData(itemIndex)._2
      if (currentValue <= randomValue) itemIndex += 1
    }
    fixedData(itemIndex)._1
  }
}