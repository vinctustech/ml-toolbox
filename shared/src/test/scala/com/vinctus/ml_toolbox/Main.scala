package com.vinctus.ml_toolbox

object Main extends App {

//  val ds = Dataset(Seq("a", "b"), Seq(Seq(11, 12), Seq(21, 22)))
  val ds = Dataset.fromCSV("test")

  println(ds)

}
