package com.vinctus.ml_toolbox

import java.awt.font.{FontRenderContext, GlyphVector}
import scala.swing.{Frame, Graphics2D, MainFrame, Panel, SimpleSwingApplication}
import scala.swing.Swing._
import java.awt.geom.{Line2D, Path2D}
import java.awt.{BasicStroke, Color, Font, RenderingHints}
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
      val plot = new Plot(0, 2 * Pi, -1, 1, 75, .5, .5)

      plot.color = Plot.ORANGE
      plot.trace(sin, 0, 2 * Pi)
      plot.color = Plot.CYAN
      plot.trace(x => sin(2 * x), 0, 2 * Pi)

      contents = new PlotPanel(plot)
      pack()
    }
}

class PlotPanel(plot: Plot) extends Panel {

  private val FRC = new FontRenderContext(null, true, false)
  private val STYLE_MAP =
    Map[Plot.Style, Int](
      Plot.PLAIN -> Font.PLAIN,
      Plot.ITALIC -> Font.ITALIC
    )

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

    // draw points
    g.setStroke(new BasicStroke(plot.pointSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))

    plot.pointsIterator foreach {
      case plot.Point(x, y, c) =>
        g.setColor(new Color(c))
        g.draw(new Line2D.Double(x, y, x, y))
    }

    // draw paths
    plot.pathsIterator foreach { p =>
      val path = new Path2D.Double
      val (x, y) = p.start

      path.moveTo(x, y)

      p.rest foreach {
        case (x, y) => path.lineTo(x, y)
      }

      g.setColor(new Color(p.color))
      g.setStroke(new BasicStroke(p.width.toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
      g.draw(path)
    }

    // draw text
    plot.textsIterator foreach {
      case plot.Text(s, x, y, color, style, pos) =>
        val gv = new Font(Font.SERIF, STYLE_MAP(style), plot.fontSize).createGlyphVector(FRC, s)
        val vb = gv.getVisualBounds
        val (xp, yp) =
          pos match {
            case Plot.ABOVE => (x - vb.getCenterX, y)
            case Plot.BELOW => (x - vb.getCenterX, y + vb.getHeight)
            case Plot.RIGHT => (x - vb.getX, y - vb.getCenterY)
            case Plot.LEFT  => (x - vb.getWidth - vb.getX, y - vb.getCenterY)
          }

        g.setColor(new Color(color))
        g.drawGlyphVector(gv, xp.toFloat, yp.toFloat)
    }
  }

}
