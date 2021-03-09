package com.vinctus.ml_toolbox

import scala.collection.immutable
import scala.collection.immutable.ArraySeq

class Row private (headings: Seq[String], headingMap: Map[String, Int], row: ArraySeq[Double])
    extends immutable.Map[String, Double]
    with (Int => Double) {
  def removed(key: String): Row = ???

  def updated[V1 >: Double](key: String, value: V1): Row = ???

  def get(key: String): Option[Double] = headingMap get key map row

  def iterator: Iterator[(String, Double)] = headings zip row iterator

  def apply(idx: Int): Double = row(idx - 1)
}
