package com.vinctus

import xyz.hyperreal.matrix.Matrix

package object ml_toolbox {

  type Vector = Matrix[Double]

  type Hypothesis = (Vector, Vector) => Double

  def mean(xs: Seq[Double]): Double = xs.sum / xs.length

  def sd(xs: Seq[Double]): Double = {
    val m = mean(xs)

    math.sqrt((xs map (x => (x - m) * (x - m)) sum) / xs.length)
  }

}
