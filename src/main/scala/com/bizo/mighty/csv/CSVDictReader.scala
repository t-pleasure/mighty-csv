package com.bizo.mighty.csv

import au.com.bytecode.opencsv.{ CSVReader => OpenCSVReader }
import java.io.{ FileReader, InputStream, InputStreamReader, FileInputStream }

/** Reads rows as Iterator[Map[String, String]]. The keys will be determined by 
 *  the header row (1st row). The header keys are then mapped to their respective data
 *  for each row. 
 *  
 *  @param reader the instance of OpenCSVReader to wrap
 */
class CSVDictReader(reader: OpenCSVReader) extends Iterator[Map[String, String]] {
  private[this] val rows: Iterator[Row] = new CSVRowIterator(reader) flatten
  val header: Row = rows.synchronized {
    if (!rows.hasNext) sys.error("No rows found") else rows.next()
  }

  override def hasNext(): Boolean = rows.synchronized {
    rows.hasNext
  }

  override def next(): Map[String, String] = rows.synchronized {
    val currentRow = rows.next()
    if (header.length != currentRow.length) {
      sys.error("Column mismatch: expected %d-cols. encountered %d-cols".format(header.length, currentRow.length))
    } else {
      Map(header.zip(currentRow): _*)
    }
  }

  def close() {
    rows.synchronized {
      reader.close()
    }
  }
}

object CSVDictReader {
  /** Creates a CSVDictReader given a file name
   *
   *  @param fname name of file to read from.  
   */
  def apply(fname: String)(implicit settings: CSVReaderSettings): CSVDictReader = {
    apply(new FileInputStream(fname))(settings)
  }

  /** Creates a CSVDictReader given an InputStream
   *  
   *  @param is InputStream to read from.
   */
  def apply(is: InputStream)(implicit settings: CSVReaderSettings): CSVDictReader = {
    val oReader = new OpenCSVReader(new InputStreamReader(is, settings.encoding),
      settings.separator,
      settings.quotechar,
      settings.escapechar,
      settings.linesToSkip,
      settings.strictQuotes,
      settings.ignoreLeadingWhiteSpace
    )
    apply(oReader)
  }

  /** Creates a CSVDictReader given an OpenCSVReader
   *  
   *  @param reader OpenCSVReader to read from
   */
  def apply(reader: OpenCSVReader): CSVDictReader = {
    new CSVDictReader(reader)
  }
}
