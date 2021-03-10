package com.vinctus.ml_toolbox

object Main extends App {

  val ds = Dataset(Seq("t", "a"), Seq(Seq(4, 2), Seq(6, 3)))
//  val ds = Dataset.fromCSV("test")

  val model = LinearRegression.train(ds)

  println(ds)
  println(model)

}
