package com.vinctus.ml_toolbox

import scala.util.Random

object RandomDataset {

  def two: Dataset = {
    val c0 = 3.7
    val c1 = 5.4
    val c2 = 7.2
    val points =
      for (_ <- 1 to 1000)
        yield {
          val x1 = Random.nextDouble() * 100
          val x2 = Random.nextDouble() * 200 + 50
          val offset = Random.nextDouble() * 2 - 1
          val y = (c0 + c1 * x1 + c2 * x2) + offset

          Seq(x1, x2, y)
        }

    Dataset(Seq("x1", "x2", "y"), points)
  }

  def quadratic: Dataset = {
    val c0 = 3.7
    val c1 = 5.4
    val c2 = 7.2
    val points =
      for (_ <- 1 to 100)
        yield {
          val x1 = Random.nextDouble() * 50
          val x2 = x1 * x1
          val offset = Random.nextDouble() * 1 - .5
          val y = (c0 + c1 * x1 + c2 * x2) + offset

          Seq(x1, x2, y)
        }

    Dataset(Seq("x1", "x2", "y"), points)
  }

}
