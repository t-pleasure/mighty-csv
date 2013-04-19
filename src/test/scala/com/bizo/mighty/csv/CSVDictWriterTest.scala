package com.bizo.mighty.csv

import org.scalatest.{ WordSpec, BeforeAndAfterAll }
import org.scalatest.matchers.ShouldMatchers
import collection.JavaConversions._
import java.io.StringWriter
import au.com.bytecode.opencsv.{ CSVWriter => OpenCSVWriter }

@org.junit.runner.RunWith(classOf[org.scalatest.junit.JUnitRunner])
class CSVDictWriterTest extends WordSpec with ShouldMatchers with BeforeAndAfterAll {
  "CSVDictWriter" should {
    "properly write header row" in {
      val output = new StringWriter()
      val writer = CSVDictWriter(new OpenCSVWriter(output), Seq("header1","header2"))
      writer.writeHeader()
      output.toString() should be ("\"header1\",\"header2\"\n")
    }
    
    "properly write header row with bytearrayoutputstream" in {
      import java.io.ByteArrayOutputStream
      val os = new ByteArrayOutputStream()
      val writer = CSVDictWriter(os, Seq("header1","header2"))
      writer.writeHeader()
      writer.close()
      os.toString() should be ("\"header1\",\"header2\"\n")
    }
    
    "properly write one row at a time in the correct ordering" in {
      val header = Seq("header1","header2","header3")
      val rows = Seq(List("a", "b", "c"), List("1", "2", "3"))
      val output = new StringWriter()
      val writer = CSVDictWriter(new OpenCSVWriter(output), header)
      writer.writeHeader()
      writer.write(Map(header.zip(rows(0)):_*))
      writer.write(Map(header.zip(rows(1)):_*))
      output.toString() should be("\"header1\",\"header2\",\"header3\"\n\"a\",\"b\",\"c\"\n\"1\",\"2\",\"3\"\n")
    }
  }
}