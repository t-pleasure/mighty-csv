package com.bizo.mighty.csv

import au.com.bytecode.opencsv.{ CSVReader => OpenCSVReader }
import java.io.{ FileReader, InputStreamReader, FileInputStream }
import scala.collection.generic._
import scala.collection.mutable.ListBuffer
import util.control.Breaks._
import com.bizo.mighty.collection.ConsecutivelyGroupable._

/**
 * Reads a CSV file into an Iteartor[Row]
 */
class CSVReader(reader: OpenCSVReader) extends Iterator[Row] {
  private[this] val rows: Iterator[Row] = new CSVRowIterator(reader) flatten

  override def hasNext(): Boolean = rows.synchronized(rows.hasNext)
  override def next(): Row = rows.synchronized(rows.next())

  def apply[T](fn: Row => T): Iterator[T] = {
    this map { fn }
  }

  def close() {
    reader.close()
  }
}

object CSVReader {
  def apply(fname: String, encoding: String = "UTF-8"): CSVReader = {
    apply(new OpenCSVReader(new InputStreamReader(new FileInputStream(fname), encoding)))
  }

  def apply(reader: OpenCSVReader): CSVReader = {
    new CSVReader(reader)
  }

  /**
   * Attempts to read in CSV file and automatically binds row to instance of T
   * note: T must have an appropriate constructor that takes in String and
   * contains the same number of arguments as the number of columns in the row.
   */
  def readAs[T: Manifest](fname: String): Iterator[T] = {
    apply(fname) { convertRow[T](_) }
  }

  /**
   * Constructs an instance of T from Row by exhaustively searching through T's
   * constructor for appropriate match.
   */
  def convertRow[T: Manifest](row: Row): T = {
    if (row == null || manifest[T] == manifest[Row]) {
      row.asInstanceOf[T]
    } else {
      // attempt to bind row to T's constructor
      var res: Option[T] = None
      manifest[T].erasure.getConstructors() foreach { constructor =>
        try {
          res = Some(constructor.newInstance(row: _*).asInstanceOf[T])
        } catch {
          case e: IllegalArgumentException => {}
        }
      }
      res.getOrElse { throw new IllegalArgumentException() }
    }
  }
}