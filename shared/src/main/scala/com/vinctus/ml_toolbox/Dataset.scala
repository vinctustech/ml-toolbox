package com.vinctus.ml_toolbox

import xyz.hyperreal.csv.CSVRead
import xyz.hyperreal.matrix.Matrix
import xyz.hyperreal.table.TextTable

import scala.collection.immutable.ArraySeq

class Dataset private (columns: ArraySeq[String], columnMap: Map[String, Int], val data: Matrix[Double])
    extends (Int => Dataset) {
  def this(columns: collection.Seq[String], data: Matrix[Double]) =
    this(columns to ArraySeq, columns.zipWithIndex toMap, data)

  require(columns.nonEmpty, "require at least one column")
  require(data.cols == columns.length, "require number of data columns equal number of column names")

  def apply(ridx: Int): Dataset = new Dataset(columns, columnMap, data.row(ridx))

  def rows: Int = data.rows

  def cols: Int = data.cols

  def row(ridx: Int): (Vector, Double) = (data.row(ridx).removeCol(data.cols), data.row(ridx).last)

  override def toString: String =
    new TextTable() {
      headerSeq(columns)

      for (i <- 1 to data.rows)
        rowSeq(data.row(i))

      1 to data.cols foreach rightAlignment
    }.toString

}

object Dataset {

  def apply(columns: collection.Seq[String], data: Matrix[Double]): Dataset =
    new Dataset(columns, data)

  def apply(columns: collection.Seq[String], data: Seq[Seq[Any]]): Dataset =
    new Dataset(columns, Matrix.fromArray(data map (_ map (_.asInstanceOf[Number].doubleValue) toArray) toArray))

  def fromCSV(file: String, columns: collection.Seq[String] = null): Dataset = {
    val csv = CSVRead.fromFile(file).get
    val (header, data) =
      if (columns eq null) (csv.head, csv drop 1)
      else (columns, csv)

    Dataset(header, data map (_ map (_.toDouble)))
  }

}
