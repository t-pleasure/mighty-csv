package com.bizo.mighty.csv

import java.io._
import au.com.bytecode.opencsv.{ CSVWriter => OpenCSVWriter }



/** Wrapper for OpenCSVWritter. Allows for writing of Array[String] and Seq[String] */
class CSVWriter(writer: OpenCSVWriter) {
  /** writes output given Array[String] */
  def write(row: Array[String]) { writer.synchronized { writer.writeNext(row) } }
  /** writes output given Seq[String] */
  def write(row: Seq[String]) { writer.synchronized { writer.writeNext(row.toArray) } }

  def close() { writer.close() }

  def flush() { writer.synchronized { writer.flush() } }

}

object CSVWriter {
  /** Creates CSVWriter outputting to file specified by fname
   *  
   *  @param fname filepath to output to.
   */
  def apply(fname: String)(implicit settings: CSVWriterSettings): CSVWriter = {
    apply(new FileOutputStream(fname))(settings)
  }

  /** Creates CSVWriter outputting to outputstream specified by os
   * 
   *  @param os OutputStream to output to.
   */
  def apply(os: OutputStream)(implicit settings: CSVWriterSettings): CSVWriter = {
    val oWriter = new OpenCSVWriter(new BufferedWriter(new OutputStreamWriter(os, settings.encoding)),
      settings.separator,
      settings.quotechar,
      settings.escapechar,
      settings.lineEnd
    )
    apply(oWriter)
  }

  /** Creates CSVWriter from OpenCSVWriter
   * 
   *  @param writer OpenCSVWriter to write to.
   */
  def apply(writer: OpenCSVWriter): CSVWriter = new CSVWriter(writer)
}
