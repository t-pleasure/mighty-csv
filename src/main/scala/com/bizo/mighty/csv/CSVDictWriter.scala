package com.bizo.mighty.csv

import java.io._
import au.com.bytecode.opencsv.{ CSVWriter => OpenCSVWriter }

/** 
 * Allows for writing rows with Map[String,String] objects.
 * headers -- specifies the list of values to extract from Map[String,String] objects.
 *            Also specifies the column ordering of the output.
 */
class CSVDictWriter(writer: OpenCSVWriter, headers: Seq[String]) {
  
  /** writes the header */
  def writeHeader() { writer.writeNext(headers.toArray) }
  
  /** writes a row */
  def write(row: Map[String, String]) { 
    val rowData: Array[String] = headers map { col: String =>
      row.get(col) getOrElse sys.error("Column (%s) not found in row [%s]".format(col, row.toString))
    } toArray
    
    writer.writeNext(rowData)
  }

  def close() { writer.close() }

  def flush() { writer.synchronized { writer.flush() } }

}

object CSVDictWriter {
  /** Creates CSVDictWriter outputting to file specified by fname
   *  
   *  @param fname filepath to output to.
   */
  def apply(fname: String, headers: Seq[String])(implicit settings: CSVWriterSettings): CSVDictWriter = {
    apply(new FileOutputStream(fname), headers)
  }

  /** Creates CSVWriter outputting to outputstream specified by os
   * 
   *  @param os OutputStream to output to.
   */
  def apply(os: OutputStream, headers: Seq[String])(implicit settings: CSVWriterSettings): CSVDictWriter = {
    val oWriter = new OpenCSVWriter(new BufferedWriter(new OutputStreamWriter(os, settings.encoding)),
      settings.separator,
      settings.quotechar,
      settings.escapechar,
      settings.lineEnd
    )
    apply(oWriter, headers)
  }

  /** Creates CSVWriter from OpenCSVWriter
   * 
   *  @param writer OpenCSVWriter to write to.
   */
  def apply(writer: OpenCSVWriter, headers: Seq[String]): CSVDictWriter = new CSVDictWriter(writer, headers)
}
