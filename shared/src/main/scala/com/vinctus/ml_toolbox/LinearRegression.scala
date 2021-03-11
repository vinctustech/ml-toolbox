package com.vinctus.ml_toolbox

import com.vinctus.ml_toolbox.LinearRegression.train
import xyz.hyperreal.matrix.Matrix

object LinearRegression {

  val hypothesis: Hypothesis = _ dot _

  val cost: (Dataset, Vector) => Double = (ds: Dataset, coefs: Vector) => {
    var cost = 0D

    for (i <- 1 to ds.rows) {
      val (features, target) = ds row i
      val error = hypothesis(coefs, features) - target

      cost += error * error
    }

    cost / 2 / ds.rows
  }

  def train(ds: Dataset,
            alpha: Double = .5,
            lambda: Double = .5,
            iterations: Int = 500,
            descend: GradientDescent = BatchGradientDescent): LinearRegression = {
    require(ds.cols >= 2, "require a dataset with at least two columns to train a model")

    val (m, s) = 1 to /*until*/ ds.cols map ds.data.col map (c => (mean(c), sd(c))) unzip
    val standardized =
      ds.transform((r, c) => /*if (c == ds.data.cols) ds.data(r, c)
          else*/ (ds.data(r, c) - m(c - 1)) / s(c - 1))

    var coefs: Vector = Matrix.col(Seq.fill(ds.cols)(0D): _*)

    for (_ <- 1 to iterations)
      coefs -= descend(standardized, coefs, alpha, hypothesis)

    val tcoefs =
      coefs.build({
        case (1, _) =>
          coefs(1, 1) * s.last + m.last - (2 to coefs.rows).map(i => coefs(i, 1) * s.last * m(i - 2) / s(i - 2)).sum
        case (i, _) => coefs(i, 1) * s.last / s(i - 2)
      })

    new LinearRegression(ds, tcoefs)
  }

}

class LinearRegression private (ts: Dataset, val coefficients: Vector) {

  def retrain(learningRate: Double = .5,
              lambda: Double = .5,
              iterations: Int = 100,
              optimize: GradientDescent): LinearRegression =
    train(ts, learningRate, lambda, iterations, optimize)

  def summary(): Unit = {}

  def cost(ds: Dataset): Double = { LinearRegression.cost(ds, coefficients) }

  def predict(features: Seq[Double]): Double = LinearRegression.hypothesis(coefficients, Matrix(features).prepend(ONE))

  override def toString: String = s"coefficients: [${coefficients mkString ", "}], cost: ${cost(ts)}"
}
