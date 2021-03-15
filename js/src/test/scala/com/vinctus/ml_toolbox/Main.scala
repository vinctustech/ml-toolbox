package com.vinctus.ml_toolbox

import org.scalajs.dom
import org.scalajs.dom.{TextMetrics, console, document, html}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

object Main extends App {

//  appendPar(document.body, "Hello World")
//
//  def appendPar(targetNode: dom.Node, text: String): Unit = {
//    val parNode = document.createElement("p")
//
//    parNode.textContent = text
//    targetNode.appendChild(parNode)
//  }

  val canvas = document.getElementById("myCanvas")

  happy(canvas.asInstanceOf[html.Canvas])

  def happy(c: html.Canvas): Unit = {
    type Ctx2D = dom.CanvasRenderingContext2D

    val ctx = c.getContext("2d").asInstanceOf[Ctx2D]
    val w = 300

    c.width = w
    c.height = w

    ctx.font = "10px serif"

    val m = ctx.measureText("asdf").asInstanceOf[js.Dynamic]

    console.log(m.actualBoundingBoxAscent)

    ctx.strokeStyle = "red"
    ctx.lineWidth = 3
    ctx.beginPath()
    ctx.moveTo(w / 3, 0)
    ctx.lineTo(w / 3, w / 3)
    ctx.moveTo(w * 2 / 3, 0)
    ctx.lineTo(w * 2 / 3, w / 3)
    ctx.moveTo(w, w / 2)
    ctx.arc(w / 2, w / 2, w / 2, 0, 3.14)

    ctx.stroke()
  }

}
