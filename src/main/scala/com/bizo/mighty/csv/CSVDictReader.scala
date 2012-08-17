package com.bizo.mighty.csv

import au.com.bytecode.opencsv.{ CSVReader => OpenCSVReader }
import java.io.{ FileReader, InputStreamReader, FileInputStream }

class CSVDictReader(reader: OpenCSVReader) extends Iterator[Map[String, String]] {
  private[this] val rows: Iterator[Row] = new CSVRowIterator(reader) flatten
  private[this] val header: Row = rows.synchronized {
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
  def apply(fname: String, encoding: String = "UTF-8"): CSVDictReader = {
    apply(new OpenCSVReader(new InputStreamReader(new FileInputStream(fname), encoding)))
  }

  def apply(reader: OpenCSVReader): CSVDictReader = {
    new CSVDictReader(reader)
  }
}