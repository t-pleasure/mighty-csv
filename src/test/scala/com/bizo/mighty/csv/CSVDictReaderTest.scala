package com.bizo.mighty.csv

import org.scalatest.{ WordSpec, BeforeAndAfterAll }
import org.scalatest.matchers.ShouldMatchers
import collection.JavaConversions._
import com.bizo.mighty.collection.ConsecutivelyGroupable._

@org.junit.runner.RunWith(classOf[org.scalatest.junit.JUnitRunner])
class CSVDictReaderTest extends WordSpec with ShouldMatchers with BeforeAndAfterAll {

  "CSVDictReader" should {
    "bind columns to headers" in {
      val rows: Seq[Map[String, String]] = CSVDictReader("src/etc/test-resources/dict-test.good.csv").toSeq
      val header = List("col1", "col2", "col3")
      header.map { rows(0).get }.flatten should be(List("a1,1", "a1,2", "a1,3"))
      header.map { rows(1).get }.flatten should be(List("a2,1", "a2,2", "a2,3"))
      header.map { rows(2).get }.flatten should be(List("a3,1", "a3,2", "a3,3"))
    }

    "fail if no rows are present in file" in {
      intercept[RuntimeException] {
        CSVDictReader("src/etc/test-resources/dict-test.empty.csv")
      }
    }

    "fail if columns do not match header" in {
      val rows: Iterator[Map[String, String]] = CSVDictReader("src/etc/test-resources/dict-test.mismatch.csv")
      // first row should be valid
      val header = List("col1", "col2", "col3")
      header.map { rows.next().get }.flatten should be(List("a1,1", "a1,2", "a1,3"))

      // 2nd row should fail
      intercept[RuntimeException] {
        rows.next()
      }
    }

  }
}