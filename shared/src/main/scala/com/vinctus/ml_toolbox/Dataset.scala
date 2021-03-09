package com.vinctus.ml_toolbox

import xyz.hyperreal.csv.CSVRead
import xyz.hyperreal.matrix.Matrix
import xyz.hyperreal.table.TextTable

import scala.collection.immutable.ArraySeq

class Dataset private (columns: ArraySeq[String], columnMap: Map[String, Int], val matrix: Matrix[Double])
    extends (Int => Dataset) {

  require(columns.nonEmpty, "require at least one column")
  require(matrix.cols == columns.length, "require number of data columns equal number of column names")

  override def apply(row: Int): Dataset = new Dataset(columns, columnMap, matrix.row(row))

  override def toString: String = {
    new TextTable() {
      headerSeq(columns)

      for (i <- 1 to matrix.rows)
        rowSeq(matrix.row(i))

      1 to matrix.cols foreach rightAlignment
    }.toString

  }

}

object Dataset {

  def apply(columns: collection.Seq[String], data: Matrix[Double]): Dataset = {
    val cols = columns to ArraySeq

    new Dataset(cols, cols zip cols.indices toMap, data)
  }

  def apply(columns: collection.Seq[String], data: Seq[Seq[Any]]): Dataset = {
    val cols = columns to ArraySeq
    val mat = Matrix.fromArray(data map (_ map (_.asInstanceOf[Number].doubleValue) toArray) toArray)

    new Dataset(cols, cols zip cols.indices toMap, mat)
  }

  def fromCSV(file: String, columns: collection.Seq[String] = null): Dataset = {
    val csv = CSVRead.fromFile(file).get
    val (header, data) =
      if (columns eq null) (csv.head, csv drop 1)
      else (columns, csv)

    Dataset(header, data map (_ map (_.toDouble)))
  }

}
