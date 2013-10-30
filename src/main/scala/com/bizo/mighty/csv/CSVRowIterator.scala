package com.bizo.mighty.csv

import au.com.bytecode.opencsv.{ CSVReader => OpenCSVReader }
import java.io.{ FileReader, InputStreamReader, FileInputStream }

/**
 * Wrapper class for OpenCSVReader to allow for Thread-safe CSV row iteration.
 * Note: This class is actually an iterator over Option[Row]. This is to
 * allow for safer/easy handling of cases where the the rows are null.
 */
class CSVRowIterator(reader: OpenCSVReader) extends Iterator[Option[Row]] {
  var nextLine: Option[Row] = Option(reader.readNext())

  override def hasNext() = nextLine.isDefined

  override def next(): Option[Row] = {
    val cur: Option[Row] = nextLine
    nextLine = Option(reader.readNext())
    cur
  }

  /** converts to Iterator[Row] */
  def asRows(): Iterator[Row] = {
    this flatten
  }

  /** alias for mapping */
  def apply[T](fn: Row => T): Iterator[T] = {
    this.flatten map { fn }
  }

  /** closes reader */
  def close() {
    reader.close()
  }
}
