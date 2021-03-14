package com.vinctus.ml_toolbox

import com.vinctus.ml_toolbox.Plot.Style
import com.vinctus.ml_toolbox.PlotPanel.glyphVector

import java.awt.{BasicStroke, Color, Font, RenderingHints}
import java.awt.font.{FontRenderContext, GlyphVector}
import java.awt.geom.{Line2D, Path2D, Rectangle2D}
import scala.swing.{Graphics2D, Panel}
import scala.swing.Swing.EtchedBorder
import scala.swing.Swing._

object PlotPanel {

  private val FRC = new FontRenderContext(null, true, false)
  private val STYLE_MAP =
    Map[Plot.Style, Int](
      Plot.PLAIN -> Font.PLAIN,
      Plot.ITALIC -> Font.ITALIC
    )

  def glyphVector(text: String, style: Style, fontSize: Int): (GlyphVector, Rectangle2D) = {
    val gv = new Font(Font.SERIF, PlotPanel.STYLE_MAP(style), fontSize).createGlyphVector(PlotPanel.FRC, text)

    (gv, gv.getVisualBounds)
  }

  def textDimensions(text: String, style: Style, fontSize: Int): (Double, Double) = {
    val (_, vb) = glyphVector(text, style, fontSize)

    (vb.getWidth, vb.getHeight)
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

    // draw points
    g.setStroke(new BasicStroke(plot.pointSize.toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))

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
        val (gv, vb) = glyphVector(s, style, plot.fontSize)
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
