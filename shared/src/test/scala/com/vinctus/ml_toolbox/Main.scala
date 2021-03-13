package com.vinctus.ml_toolbox

import scala.swing.{Frame, Graphics2D, MainFrame, Panel, SimpleSwingApplication}
import scala.swing.Swing._
import java.awt.geom.{Line2D, Path2D}
import java.awt.{BasicStroke, Color, RenderingHints}
import scala.math._

//object Main extends App {
//
//  val ds = RandomDataset.quadratic
//  val model = LinearRegression train ds
//
////  println(ds)
//  println(model)
//  println(model predict Seq(3, 9))
//
//}

object Main extends SimpleSwingApplication {
  def top: Frame =
    new MainFrame {
      val plot = new Plot(0, 2 * Pi, -1, 1, 50, 2, 20, 20)

      plot.trace(sin, 0, 2 * Pi)
      contents = new PlotPanel(plot)
      pack()
    }
}

class PlotPanel(plot: Plot) extends Panel {

  background = Color.BLACK
  border = EtchedBorder
  preferredSize = (plot.width, plot.height)

  plot.onChange = () => repaint()

  override protected def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)

    g.setRenderingHints(
      new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON))
    g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON))
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

    g.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))

    plot.pointsIterator foreach {
      case plot.Point(x, y, c) =>
        g.setColor(new Color(c))
        g.draw(new Line2D.Double(x, y, x, y))
    }

    g.setStroke(new BasicStroke(plot.lines, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))

    plot.pathsIterator foreach { p =>
      val path = new Path2D.Double
      val (x, y) = p.start

      path.moveTo(x, y)

      p.rest foreach {
        case (x, y) => path.lineTo(x, y)
      }

      g.setColor(new Color(p.color))
      g.draw(path)
    }
  }

}
