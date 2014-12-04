package monnef.core.common

case class MutableFloatVec(var x: Float, var y: Float, var z: Float) {
  def set(x: Float, y: Float, z: Float) {
    this.x = x
    this.y = y
    this.z = z
  }
}
