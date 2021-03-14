package com.vinctus.ml_toolbox

import scala.swing.{Frame, MainFrame, SimpleSwingApplication}

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

//object Main extends SimpleSwingApplication {
//  def top: Frame =
//    new MainFrame {
//      private val ds = RandomDataset.quadratic
//      private val model = LinearRegression train ds
//
//      //  println(ds)
//      println(model)
////      println(model predict Seq(3, 9))
//
//      private val x2 = ds.col("x2")
//      private val y = ds.col("y")
//      println(y.min, y.max)
//      private val plot = new Plot(x2.min, x2.max, y.min, y.max, .8, .02, 2000, 2000)
//
//      plot.color = Plot.CYAN
//      plot.trace(x => model.predict(Seq(x, x * x)), 0, 50)
//
//      contents = new PlotPanel(plot)
//      pack()
//    }
//}

object Main extends SimpleSwingApplication {
  def top: Frame =
    new MainFrame {
      private val ds = RandomDataset.one
      private val model = LinearRegression train ds

      //  println(ds)
      println(model)

      private val plot = new Plot(0, 20, 50, 130, 400, 300, PlotPanel.textDimensions, 5, 5)

      plot.color = Plot.CYAN

      ds.rowIterator foreach {
        case Seq(x1, y) => plot.point(x1, y)
      }

      plot.color = Plot.ORANGE
      plot.trace(x => model.predict(Seq(x)), 0, 20)

      contents = new PlotPanel(plot)
      pack()
    }
}
