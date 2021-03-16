package com.vinctus.ml_toolbox

import com.vinctus.ml_toolbox.Plot.Style
import org.scalajs.dom
import org.scalajs.dom.{document, html}

import scala.scalajs.js

object PlotCanvas {

  type Ctx2D = dom.CanvasRenderingContext2D

  def apply(plot: Plot, canvas: html.Canvas): Unit = {
    plot.onChange = () => apply(plot, canvas)

    val ctx = canvas.getContext("2d").asInstanceOf[Ctx2D]

    canvas.width = plot.width
    canvas.height = plot.height
    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, plot.width, plot.height)

    // draw points

    ctx.lineWidth = plot.pointSize
    ctx.strokeStyle = "green"

    plot.pointsIterator foreach {
      case plot.Point(x, y, c) =>
        ctx.fillStyle = f"#$c%06x"
        ctx.beginPath()
        ctx.arc(x, y, plot.pointSize / 2, 0, 2 * math.Pi, anticlockwise = false)
        ctx.fill()
    }

    // draw paths

    plot.pathsIterator foreach { p =>
      ctx.beginPath()
      val (x, y) = p.start

      ctx.moveTo(x, y)

      p.rest foreach {
        case (x, y) => ctx.lineTo(x, y)
      }

      ctx.strokeStyle = f"#${p.color}%06x"
      ctx.lineWidth = p.width
      ctx.stroke()
    }

  }

  def textDimensions(text: String, style: Style, fontSize: Int): (Double, Double) = {
    val canvas = document.createElement("canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d").asInstanceOf[Ctx2D]

    ctx.font = s"${if (style != Plot.PLAIN) style.name else ""} ${fontSize}px serif"

    val metrics = ctx.measureText(text).asInstanceOf[js.Dynamic]

    ((metrics.actualBoundingBoxLeft + metrics.actualBoundingBoxRight).asInstanceOf[Double],
     (metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent).asInstanceOf[Double])
  }

}
