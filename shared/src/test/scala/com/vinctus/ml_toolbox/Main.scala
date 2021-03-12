package com.vinctus.ml_toolbox

import scala.swing.{Frame, Graphics2D, Label, MainFrame, Panel, SimpleSwingApplication}
import scala.swing.Swing._

//object Main extends App {
//
//  val ds = RandomDataset.basic
//  val model = LinearRegression train ds
//
//  println(model)
//  println(model predict Seq(4, 5))
//
//}

object Main extends SimpleSwingApplication {
  def top: Frame =
    new MainFrame {
      contents = new Label("asdf")
      size = (200, 200)
    }
}

class PlotPanel(plot: Plot) extends Panel {

  private val width = (plot.xupper - plot.xlower) * plot.scale
  private val height = (plot.yupper - plot.ylower) * plot.scale

  preferredSize = (width.toInt, height.toInt)

  plot.onChange = () => repaint()

  override protected def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)

  }

}
