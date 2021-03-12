package com.vinctus.ml_toolbox

import xyz.hyperreal.matrix.Matrix
import xyz.hyperreal.matrix.Matrix.Implicits._

trait Optimizer extends ((Dataset, Vector, Double, Hypothesis, Int, Double) => Vector)

object GradientDescent extends Optimizer {
  def apply(ds: Dataset, initial: Vector, rate: Double, h: Hypothesis, iterations: Int, norm: Double): Vector = {
    var params = initial
    var i = 0
    var step: Matrix[Double] = Matrix.col(Seq.fill(ds.cols)(1D): _*)

    def done = iterations > 0 && i == iterations || step.norm < norm

    while (!done) {
      step = Matrix.col((for (j <- 1 to ds.cols) yield {
        var theta: Double = 0

        for (i <- 1 to ds.rows) {
          val (features, target) = ds.row(i)

          theta += (h(params, features) - target) * (features col j)
        }

        theta * rate / ds.rows
      }): _*)

      params -= step
      i += 1

      if (done)
        println((i, step, step.norm))
    }

    params
  }
}
