package com.vinctus.ml_toolbox

import scala.collection.mutable.ListBuffer

object Plot {
  val LIGHT_GRAY = 0xC0C0C0
}

class Plot(xlower: Double,
           xupper: Double,
           ylower: Double,
           yupper: Double,
           val scale: Double = 1,
           val lines: Int = 2,
           val xlabeling: Double = 1,
           val ylabeling: Double = 1,
           val fontSize: Int = 10) {

  var onChange: () => Unit = () => {}
  var colorInt: Int = 0xc0c0c0

  private val xlower1: Double = xlower - 2 * lines / scale
  private val xupper1: Double = xupper + 2 * lines / scale
  private val ylower1: Double = ylower - 2 * lines / scale
  private val yupper1: Double = yupper + 2 * lines / scale

  val width: Int = ((xupper1 - xlower1) * scale).toInt
  val height: Int = ((yupper1 - ylower1) * scale).toInt

  private val points = new ListBuffer[Point]
  private val paths = new ListBuffer[Path]

  def pointsIterator: Iterator[Point] = points.iterator

  def pathsIterator: Iterator[Path] = paths.iterator

  def transform(x: Double, y: Double): (Double, Double) = ((x - xlower1) * scale, height - 1 - (y - ylower1) * scale)

  def point(x: Double, y: Double): Unit = {
    val (tx, ty) = transform(x, y)

    points += Point(tx, ty, colorInt)
    onChange()
  }

  def path: Path = {
    paths += new Path
    paths.last
  }

  def trace(f: Double => Double, lower: Double, upper: Double): Unit = {
    val p = path

    var xcur: Double = 0
    var ycur: Double = 0

    def plot(x: Double): Unit = {
      xcur = x
      ycur = f(x)
      p.point(xcur, ycur)
    }

    plot(lower)

    while (xcur < upper) {
      val next = xcur + 4 / scale

      plot(if (next > upper) upper else next)
    }
  }

  def color: Int = colorInt

  def color_=(c: Int): Unit = colorInt = c

  case class Point(x: Double, y: Double, c: Int)

  class Path {
    private val points = new ListBuffer[(Double, Double)]

    val color: Int = colorInt

    def point(x: Double, y: Double): Path = {
      points += transform(x, y)
      onChange()
      this
    }

    def start: (Double, Double) = points.head

    def rest: Iterator[(Double, Double)] = points.iterator.drop(1)
  }

}
