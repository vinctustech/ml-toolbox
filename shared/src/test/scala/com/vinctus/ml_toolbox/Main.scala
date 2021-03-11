package com.vinctus.ml_toolbox

import xyz.hyperreal.matrix.Matrix

object Main extends App {

  val ds = Dataset(Seq("f", "t"), Seq(Seq(2, 4), Seq(3, 6)))
//  val ds = Dataset.fromCSV("test")

  val model = LinearRegression train ds

  println(ds)
  println(model)
  println(model predict Seq(4))

}
