package com.vinctus.ml_toolbox

import xyz.hyperreal.matrix.Matrix
import xyz.hyperreal.matrix.Matrix.Implicits._

trait GradientDescent extends ((Dataset, Vector, Double, Hypothesis) => Vector)

object BatchGradientDescent extends GradientDescent {
  def apply(ds: Dataset, v: Vector, rate: Double, h: Hypothesis): Vector = {
    Matrix.col((for (j <- 1 to ds.cols) yield {
      var theta: Double = 0

      for (i <- 1 to ds.rows) {
        val (features, target) = ds.row(i)

        theta += (h(v, features) - target) * (features col j)
      }

      theta * rate / ds.rows
    }): _*)
  }
}
