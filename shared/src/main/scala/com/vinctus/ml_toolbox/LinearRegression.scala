package com.vinctus.ml_toolbox

import com.vinctus.ml_toolbox.LinearRegression.train
import xyz.hyperreal.matrix.Matrix

object LinearRegression {

  val hypothesis: Hypothesis = _ dot _

  val cost: (Dataset, Vector) => Double = (ds: Dataset, coefs: Vector) => {
    var cost = 0D

    for (i <- 1 to ds.rows) {
      val (features, target) = ds datum i
      val error = hypothesis(coefs, features) - target

      cost += error * error
    }

    cost / 2 / ds.rows
  }

  def train(ds: Dataset,
            alpha: Double = .5,
            lambda: Double = .5,
            iterations: Int = 100000,
            norm: Double = 1E-9,
            optimize: Optimizer = GradientDescent,
            standardize: Boolean = true,
            standardizeTarget: Boolean = true): LinearRegression = {
    require(ds.cols >= 2, "require a dataset with at least two columns to train a model")

    val (m, s) =
      if (standardize)
        (if (standardizeTarget) 1 to ds.cols else 1 until ds.cols) map ds.data.col map (c => (mean(c), sd(c))) unzip
      else (null, null)
    val transformed =
      if (standardize)
        ds.transform(
          (r, c) =>
            if (!standardizeTarget && c == ds.data.cols) ds.data(r, c)
            else (ds.data(r, c) - m(c - 1)) / s(c - 1))
      else
        ds

    val coefs = optimize(transformed, Matrix.col(Seq.fill(ds.cols)(0D): _*), alpha, hypothesis, iterations, norm)
    val tcoefs =
      if (standardize)
        if (standardizeTarget)
          coefs.build({
            case (1, _) =>
              coefs(1, 1) * s.last + m.last - (2 to coefs.rows).map(i => coefs(i, 1) * s.last * m(i - 2) / s(i - 2)).sum
            case (i, _) => coefs(i, 1) * s.last / s(i - 2)
          })
        else
          coefs.build({
            case (1, _) =>
              coefs(1, 1) - (2 to coefs.rows).map(i => coefs(i, 1) * m(i - 2) / s(i - 2)).sum
            case (i, _) => coefs(i, 1) / s(i - 2)
          })
      else
        coefs

    new LinearRegression(ds, tcoefs)
  }

}

class LinearRegression private (ts: Dataset, val coefficients: Vector) {

  def retrain(learningRate: Double = .5,
              lambda: Double = .5,
              iterations: Int = 100000,
              norm: Double = 1E-9,
              optimize: Optimizer,
              standardize: Boolean = true,
              standardizeTarget: Boolean = true): LinearRegression =
    train(ts, learningRate, lambda, iterations, norm, optimize, standardize, standardizeTarget)

  def summary(): Unit = {}

  def cost(ds: Dataset): Double = { LinearRegression.cost(ds, coefficients) }

  def predict(features: Seq[Double]): Double = LinearRegression.hypothesis(coefficients, Matrix(features).prepend(ONE))

  override def toString: String = s"coefficients: [${coefficients mkString ", "}], cost: ${cost(ts)}"
}
