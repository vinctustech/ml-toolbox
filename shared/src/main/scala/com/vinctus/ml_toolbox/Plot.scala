package com.vinctus.ml_toolbox

import scala.collection.mutable.ListBuffer

object Plot {
  val LIGHT_GRAY = 0xC0C0C0
}

class Plot(val xlower: Double,
           val xupper: Double,
           val ylower: Double,
           val yupper: Double,
           val scale: Double = 1,
           val xlabeling: Double = 1,
           val ylabeling: Double = 1,
           val fontSize: Int = 10) {

  var onChange: () => Unit = () => {}
  var colorInt: Int = 0xc0c0c0

  val width: Int = ((xupper - xlower) * scale).toInt
  val height: Int = ((yupper - ylower) * scale).toInt

  private val points = new ListBuffer[Point]
  private val paths = new ListBuffer[Path]

  def pointsIterator: Iterator[Point] = points.iterator

  def pathsIterator: Iterator[Path] = paths.iterator

  def transform(x: Double, y: Double): (Double, Double) = ((x - xlower) * scale, height - 1 - (y - ylower) * scale)

  def point(x: Double, y: Double): Unit = {
    val (tx, ty) = transform(x, y)

    points += Point(tx, ty, colorInt)
    onChange()
  }

  def path: Path = {
    paths += new Path
    paths.last
  }

  def color: Int = colorInt

  def color_=(c: Int): Unit = colorInt = c

  case class Point(x: Double, y: Double, c: Int)

  class Path {
    private val points = new ListBuffer[(Double, Double)]

    def point(x: Double, y: Double): Path = {
      points += transform(x, y)
      onChange()
      this
    }

    def first: (Double, Double) = points.head

    def rest: Iterator[(Double, Double)] = points.iterator.drop(1)
  }

}
