package com.bizo.mighty.csv

import org.scalatest.{ WordSpec, BeforeAndAfterAll }
import org.scalatest.matchers.ShouldMatchers
import collection.JavaConversions._
import com.bizo.mighty.collection.ConsecutivelyGroupable._

@org.junit.runner.RunWith(classOf[org.scalatest.junit.JUnitRunner])
class CSVReaderTest extends WordSpec with ShouldMatchers with BeforeAndAfterAll {
  import CSVReaderTest._
  import CSVReader._

  "CSVReader" should {

    val expectedData = Seq(Array("1", "bizo", "row1"),
      Array("2", "bizo", "row2"),
      Array("3", "bizov2", "row3"),
      Array("4", "bizov2", "row4"),
      Array("5", "bizo", "row5"))

    "properly iterate through a csv file" in {
      CSVReader("src/etc/test-resources/test.csv").zipWithIndex foreach {
        case (row, index) =>
          row should be(expectedData(index))
      }
    }

    "properly iterate through a csv file, after skipping lines (test 'explicit' defaults)" in {
      CSVReader("src/etc/test-resources/test.csv")(CSVReaderSettings.Standard.copy(linesToSkip = 3)).zipWithIndex foreach {
        case (row, index) =>
          row should be(expectedData(index + 3))
      }
    }

    "properly bind to specified Types" in {
      //tests case class
      CSVReader.readAs[Foo3S]("src/etc/test-resources/test.csv").toList should be(
        expectedData.map { row => Foo3S(row(0), row(1), row(2)) })

      //tests normal class that contains all string constructor
      CSVReader.readAs[FooMixed]("src/etc/test-resources/test.csv").toList.
        map(row => List(row.x.toString, row.y, row.z)) should be(
          expectedData.map { _.toList })
    }

    "properly perform row conversion via apply method" in {
      CSVReader("src/etc/test-resources/test.csv")(CSVReaderSettings.Standard) { case Array(x, y, z) => Foo3S(x, y, z) }.toList should be(
        expectedData.map { row => Foo3S(row(0), row(1), row(2)) })
    }

    "throw an exception when rows are inconsistent with type constructors" in {
      intercept[IllegalArgumentException] {
        CSVReader.readAs[Foo3S]("src/etc/test-resources/two-col.csv").toList
      }
    }

    "properly perform sequential group by on the 2nd column" in {
      val expectedGroups: Seq[RowGroup[String]] = Seq(
        ("bizo", Seq(expectedData(0), expectedData(1))),
        ("bizov2", Seq(expectedData(2), expectedData(3))),
        ("bizo", Seq(expectedData(4))))

      val keyfunc = (row: Array[String]) => row(1)

      CSVReader("src/etc/test-resources/test.csv").consecutiveGrouping(keyfunc).zipWithIndex foreach {
        case ((key, group: Seq[_]), indx) =>
          val (eKey, eGroup: Seq[_]) = expectedGroups(indx)
          // test key validity
          key should be(eKey)

          // test grouped row validity
          eGroup zip group foreach {
            case (g1, g2) =>
              g1 should be === g2
          }
      }
    }

  }

  "CSVReader.convertRow" should {
    "handle null input" in {
      Option(CSVReader.convertRow[Int](null)) should be(None)
    }

    "handle [Row] input" in {
      CSVReader.convertRow[Row](Array("1", "2")) should be(Array("1", "2"))
    }
  }
}

/**
 * Companion class
 */
object CSVReaderTest {
  /**
   * Classes used in testing binding
   */
  final case class Foo3S(x: String, y: String, z: String)

  final class FooMixed(val x: Int, val y: String, val z: String) {
    // de-serialization method to be used by CSVReader
    def this(x: String, y: String, z: String) = this(x.toInt, y, z)
  }
}
