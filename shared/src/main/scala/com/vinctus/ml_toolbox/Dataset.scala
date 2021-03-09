package com.vinctus.ml_toolbox

import scala.collection.immutable.ArraySeq

object Dataset {

  def fromCSV(file: String) = {}

}

class Dataset(headings: Seq[String], rows: Seq[ArraySeq[Double]]) {

  require(headings.nonEmpty, "require at least one heading")

  private val headingsLen = headings.length

  require(rows.forall(_.length == headingsLen), "require row lengths equal number of headings")

  private val headingMap: Map[String, Int] = headings zip headings.indices toMap
  private val data = rows to ArraySeq

  override def toString: String = super.toString

}
