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
            alpha: Double = .1,
            lambda: Double = .5,
            iterations: Int = 400,
            optimize: Optimizer = GradientDescent): LinearRegression = {
    require(ds.cols >= 2, "require a dataset with at least two columns to train a model")

    val (m, s) = 1 to /*until*/ ds.cols map ds.data.col map (c => (mean(c), sd(c))) unzip
    val standardized =
      ds.transform((r, c) => /*if (c == ds.data.cols) ds.data(r, c)
          else*/ (ds.data(r, c) - m(c - 1)) / s(c - 1))

//    println(standardized, m, s)

    val sds = standardized

    var coefs: Vector = Matrix.col(Seq.fill(ds.cols)(0D): _*)

    for (_ <- 1 to iterations)
      coefs -= optimize(sds, coefs, alpha, hypothesis)

//    val coefs1 = coefs.drop(1).zipWithIndex map { case (c, i) => c * s(i) + m(i) }

    new LinearRegression(sds, coefs, m, s)
  }

}

class LinearRegression private (ts: Dataset, val coefficients: Vector, m: Seq[Double], s: Seq[Double]) {

  def retrain(learningRate: Double = .1,
              lambda: Double = .5,
              iterations: Int = 400,
              optimize: Optimizer): LinearRegression =
    train(ts, learningRate, lambda, iterations, optimize)

  def summary(): Unit = {}

  def cost(ds: Dataset): Double = { LinearRegression.cost(ds, coefficients) }

  def predict(features: Seq[Double]): Double =
    (Matrix(1D +: (features.zipWithIndex map { case (f, i) => (f - m(i)) / s(i) })) dot coefficients) * s.last + m.last

  override def toString: String = s"coefficients: [${coefficients mkString ", "}], cost: ${cost(ts)}"
}
