package com.vinctus.ml_toolbox

import scala.collection.mutable.ListBuffer

class Plot(val xlower: Double,
           val xupper: Double,
           val ylower: Double,
           val yupper: Double,
           val scale: Double = 1,
           val xlabeling: Double = 1,
           val ylabeling: Double = 1,
           val fontSize: Int = 10) {

  var onChange: () => Unit = () => {}

  private val points = new ListBuffer[Point]
  private val paths = new ListBuffer[Path]

  def point(x: Double, y: Double): Unit = {
    points += Point(x, y)
    onChange()
  }

  def start: Path = {
    paths += new Path
    paths.last
  }

  case class Point(x: Double, y: Double)

  class Path {
    private val points = new ListBuffer[Point]

    def add(x: Double, y: Double): Path = {
      points += Point(x, y)
      onChange()
      this
    }

    def iterator: Iterator[Point] = points.iterator
  }

}
