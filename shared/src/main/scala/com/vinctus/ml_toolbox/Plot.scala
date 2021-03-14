package com.vinctus.ml_toolbox

import com.vinctus.ml_toolbox.Plot.{LIGHT_GRAY, Position, Style}

import scala.collection.mutable.ListBuffer
import scala.math.BigDecimal.double2bigDecimal

object Plot {
  val LIGHT_GRAY = 0xC0C0C0
  val RED = 0xFF0000
  val ORANGE = 0xFFA500
  val GREEN = 0x00FF00
  val BLUE = 0x0000FF
  val CYAN = 0x00FFFF

  abstract class Position
  case object RIGHT extends Position
  case object LEFT extends Position
  case object BELOW extends Position
  case object ABOVE extends Position
  case object BASELINE extends Position

  abstract class Style
  case object ITALIC extends Style
  case object PLAIN extends Style
}

class Plot(xlower: Double,
           xupper: Double,
           ylower: Double,
           yupper: Double,
           textDimensions: (String, Style, Int) => (Double, Double),
           xscale: Double = 1,
           yscale: Double = 1,
           xlabels: Double = 1,
           ylabels: Double = 1,
           val grid: Boolean = true,
           val pointSize: Int = 8,
           val fontSize: Int = 12) {

  val DEFAULT_LINE_WIDTH = 2

  var onChange: () => Unit = () => {}
  var colorInt: Int = LIGHT_GRAY
  var lineWidth: Double = DEFAULT_LINE_WIDTH

  private val xLineMargin = px(4)
  private val yLineMargin = py(4)

  private val xstart = xlower.ceil
  private val xend = xupper.floor
  private val ystart = ylower.ceil
  private val yend = yupper.floor

  private val points = new ListBuffer[Point]
  private val paths = new ListBuffer[Path]
  private val texts = new ListBuffer[Text]
  private val xlower1: Double = xlower - 3.5 * xLineMargin - px(textDimensions(label(yend), Plot.PLAIN, fontSize)._1)
  private val xupper1: Double = xupper + xLineMargin + px(textDimensions(label(xend), Plot.PLAIN, fontSize)._1 / 2)
  private val ylower1: Double = ylower - 3 * yLineMargin - py(textDimensions("0", Plot.PLAIN, fontSize)._2)
  private val yupper1: Double = yupper + yLineMargin + py(textDimensions("0", Plot.PLAIN, fontSize)._2 / 2)

  val width: Int = ((xupper1 - xlower1) * xscale).toInt
  val height: Int = ((yupper1 - ylower1) * yscale).toInt

  private def label(v: BigDecimal) =
    if (v.isWhole) v.toBigInt.toString
    else v.toString

  private def px(p: Double) = p / xscale

  private def py(p: Double) = p / yscale

  line(xstart, ylower - py(4), xupper, ylower - py(4))

  for (x <- xstart to xend by xlabels) {
    line(x.toDouble, ylower - py(4), x.toDouble, ylower - py(6))
    text(label(x), x.toDouble, ylower - py(10), Plot.PLAIN, Plot.BELOW)
  }

  line(xlower - px(4), ystart, xlower - px(4), yupper)

  for (y <- ystart to yend by ylabels) {
    line(xlower - px(4), y.toDouble, xlower - px(6), y.toDouble)
    text(label(y), xlower - px(10), y.toDouble, Plot.PLAIN, Plot.LEFT)
  }

  lines = .1

  for (x <- xstart to xend by xlabels)
    line(x.toDouble, ylower, x.toDouble, yupper)

  for (y <- ystart to yend by ylabels)
    line(xlower, y.toDouble, xupper, y.toDouble)

  lines = DEFAULT_LINE_WIDTH

  def pointsIterator: Iterator[Point] = points.iterator

  def pathsIterator: Iterator[Path] = paths.iterator

  def textsIterator: Iterator[Text] = texts.iterator

  def transform(x: Double, y: Double): (Double, Double) = ((x - xlower1) * xscale, height - 1 - (y - ylower1) * yscale)

  def point(x: Double, y: Double): Unit = {
    val (tx, ty) = transform(x, y)

    points += Point(tx, ty, colorInt)
    onChange()
  }

  def text(s: String, x: Double, y: Double, style: Style, pos: Position): Unit = {
    val (tx, ty) = transform(x, y)

    texts += Text(s, tx, ty, colorInt, style, pos)
    onChange()
  }

  def path: Path = {
    paths += new Path
    paths.last
  }

  def line(x1: Double, y1: Double, x2: Double, y2: Double): Path = path.point(x1, y1).point(x2, y2)

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
      val next = xcur + px(4)

      plot(if (next > upper) upper else next)
    }
  }

  def color: Int = colorInt

  def color_=(c: Int): Unit = colorInt = c

  def lines: Double = lineWidth

  def lines_=(w: Double): Unit = lineWidth = w

  case class Text(s: String, x: Double, y: Double, color: Int, style: Style, pos: Position)

  case class Point(x: Double, y: Double, c: Int)

  class Path {
    private val points = new ListBuffer[(Double, Double)]

    val color: Int = colorInt
    val width: Double = lineWidth

    def point(x: Double, y: Double): Path = {
      points += transform(x, y)
      onChange()
      this
    }

    def start: (Double, Double) = points.head

    def rest: Iterator[(Double, Double)] = points.iterator.drop(1)
  }

}
