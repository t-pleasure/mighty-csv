package com.bizo.mighty.csv

/** Settings for CSVWriter */
trait CSVWriterSettings {
  val separator: Char
  val quotechar: Char
  val escapechar: Char
  val lineEnd: String
  val encoding: String
}

object CSVWriterSettings{
  import au.com.bytecode.opencsv.{ CSVWriter => OpenCSVWriter }
  import OpenCSVWriter._
  val DEFAULT_ENCODING = "UTF-8"

  case class Standard(
    override val separator: Char                  = DEFAULT_SEPARATOR,
    override val quotechar: Char                  = DEFAULT_QUOTE_CHARACTER,
    override val escapechar: Char                 = DEFAULT_ESCAPE_CHARACTER,
    override val lineEnd: String                  = DEFAULT_LINE_END,
    override val encoding: String                 = DEFAULT_ENCODING
    ) extends CSVWriterSettings

  /** The Standard/default Writer settings */
  implicit object Standard extends Standard(
      DEFAULT_SEPARATOR,
      DEFAULT_QUOTE_CHARACTER,
      DEFAULT_ESCAPE_CHARACTER,
      DEFAULT_LINE_END,
      DEFAULT_ENCODING
   )
}
