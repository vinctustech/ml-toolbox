package com.vinctus.ml_toolbox

import xyz.hyperreal.matrix.Matrix

import util.Random

object Main extends App {

//  val ds = Dataset(Seq("f", "t"), Seq(Seq(2, 4), Seq(3, 6)))
//  val ds = Dataset.fromCSV("test")

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

  val ds = Dataset(Seq("x1", "x2", "y"), points)
  val model = LinearRegression train ds

//  println(ds)
  println(model)
  println(model predict Seq(4, 5))

}
