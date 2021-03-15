package com.vinctus.ml_toolbox

import com.vinctus.ml_toolbox.Plot.Style
import org.scalajs.dom
import org.scalajs.dom.{document, html}

object PlotCanvas {

  type Ctx2D = dom.CanvasRenderingContext2D

  def apply(plot: Plot, canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[Ctx2D]

    canvas.width = plot.width
    canvas.height = plot.height

  }

  def textDimensions(text: String, style: Style, fontSize: Int): (Double, Double) = {
    val canvas = document.createElement("canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d").asInstanceOf[Ctx2D]

  }

}
