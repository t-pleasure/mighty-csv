package com.bizo.mighty.csv

/** Settings Trait for CSVReader */
trait CSVReaderSettings {
  val separator: Char
  val quotechar: Char
  val escapechar: Char
  val linesToSkip: Int
  val strictQuotes: Boolean
  val ignoreLeadingWhiteSpace: Boolean
  val encoding: String
}

object CSVReaderSettings {
  import au.com.bytecode.opencsv.{CSVReader => OpenCSVReader}
  import au.com.bytecode.opencsv.CSVParser._
  import OpenCSVReader.DEFAULT_SKIP_LINES
  
  val DEFAULT_ENCODING = "UTF-8"

  case class Standard(
    override val separator: Char                  = DEFAULT_SEPARATOR,
    override val quotechar: Char                  = DEFAULT_QUOTE_CHARACTER,
    override val escapechar: Char                 = DEFAULT_ESCAPE_CHARACTER,
    override val linesToSkip: Int                 = DEFAULT_SKIP_LINES,
    override val strictQuotes: Boolean            = DEFAULT_STRICT_QUOTES,
    override val ignoreLeadingWhiteSpace: Boolean = DEFAULT_IGNORE_LEADING_WHITESPACE,
    override val encoding: String                 = DEFAULT_ENCODING
    ) extends CSVReaderSettings

  /** The Standard/default Reader Settings */
  implicit object Standard extends Standard(
      DEFAULT_SEPARATOR,
      DEFAULT_QUOTE_CHARACTER,
      DEFAULT_ESCAPE_CHARACTER,
      DEFAULT_SKIP_LINES,
      DEFAULT_STRICT_QUOTES,
      DEFAULT_IGNORE_LEADING_WHITESPACE,
      DEFAULT_ENCODING
   )
}
