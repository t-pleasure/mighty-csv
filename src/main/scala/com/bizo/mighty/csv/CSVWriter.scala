package com.bizo.mighty.csv

import java.io._
import au.com.bytecode.opencsv.{ CSVWriter => OpenCSVWriter }

class CSVWriter(writer: OpenCSVWriter) {
  /** writes a row*/
  def write(row: Array[String]) { writer.synchronized { writer.writeNext(row) } }
  /** writes a row*/
  def write(row: Seq[String]) { writer.synchronized { writer.writeNext(row.toArray) } }

  def close() { writer.close() }

  def flush() { writer.synchronized { writer.flush() } }

}

object CSVWriter {
  /** helper methods to invoke constructor */
  def apply(fname: String, encoding: String = "UTF-8"): CSVWriter = {
    apply(new OpenCSVWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname), encoding))))
  }

  def apply(writer: OpenCSVWriter): CSVWriter = new CSVWriter(writer)
}