package com.vinctus.ml_toolbox

import scala.swing.{Frame, Graphics2D, Label, MainFrame, Panel, Point, SimpleSwingApplication}
import scala.swing.Swing._
import java.awt.geom.{Line2D, Path2D, Point2D}
import java.awt.{BasicStroke, Color, RenderingHints}

object Main extends App {

  val ds = RandomDataset.quadratic
  val model = LinearRegression train ds

//  println(ds)
  println(model)
  println(model predict Seq(3, 9))

}

//object Main extends SimpleSwingApplication {
//  def top: Frame =
//    new MainFrame {
//      val plot = new Plot(-100, 100, -100, 100, 2, 20, 20)
//
//      plot.point(0, 0)
//
//      val p = plot.path
//
//      p.point(0, 0).point(20, 20)
//      contents = new PlotPanel(plot)
//      pack()
//    }
//}

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

    g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))

    plot.pathsIterator foreach { p =>
      val path = new Path2D.Double
      val (x, y) = p.first

      path.moveTo(x, y)
      p.rest foreach {
        case (x, y) => path.lineTo(x, y)
      }

      g.setColor(new Color(p.color))
      g.draw(path)
    }
  }

}
