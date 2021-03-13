package com.vinctus.ml_toolbox

import com.vinctus.ml_toolbox.Plot.{LIGHT_GRAY, Position, Style}

import java.awt.Font
import scala.collection.mutable.ListBuffer

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
           val scale: Double = 1,
           val xlabeling: Double = 1,
           val ylabeling: Double = 1,
           val fontSize: Int = 12) {

  var onChange: () => Unit = () => {}
  var colorInt: Int = LIGHT_GRAY
  var lineWidth: Double = 2

  private val lineMargin = 5 / scale
  private val xlower1: Double = xlower - lineMargin - 20 / scale
  private val xupper1: Double = xupper + lineMargin
  private val ylower1: Double = ylower - lineMargin - 20 / scale
  private val yupper1: Double = yupper + lineMargin

  val width: Int = ((xupper1 - xlower1) * scale).toInt
  val height: Int = ((yupper1 - ylower1) * scale).toInt

  private val points = new ListBuffer[Point]
  private val paths = new ListBuffer[Path]
  private val texts = new ListBuffer[Text]

  def pointsIterator: Iterator[Point] = points.iterator

  def pathsIterator: Iterator[Path] = paths.iterator

  def textsIterator: Iterator[Text] = texts.iterator

  def transform(x: Double, y: Double): (Double, Double) = ((x - xlower1) * scale, height - 1 - (y - ylower1) * scale)

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
