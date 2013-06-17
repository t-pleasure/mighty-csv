mighty-csv
==========

mighty-csv is a csv utility for scala. Currently, it acts as a scala wrapper around the OpenCSV project (http://opencsv.sourceforge.net/). 

Benefits of using mighty-csv (as opposed to just OpenCSV):
----------
* Reader implments Iterator. This means that we can call foreach, map, fold, etc...
* Implements a reader and writer class for csv files with headers.
* Allows for automatic binding of rows to classes.


SBT
----------
Tell sbt about a dependency on mighty-csv by adding a library dependency to your build.sbt file (or a Scala build file).
If you are using Scala 2.9, add the following:

    libraryDependencies += "com.bizo" % "mighty-csv_2.9.1" % "0.2"
    
If you are using Scala 2.10, add the following:    

    libraryDependencies += "com.bizo" % "mighty-csv_2.10" % "0.2"


Reading CSV Files
----------
### Reading csv files without headers (rows are Array[String])
    import com.bizo.mighty.csv.CSVReader
    
    // reading from filepath
    val rows: Iterator[Array[String]] = CSVReader("filename.csv")
    
    // reading from InputStream
    val inStream = {... get input stream ...}
    val rows: Iterator[Array[String]] = CSVReader(inStream)
    
### Reading csv files with headers (rows are Map[String, String])

    import com.bizo.mighty.csv.CSVDictReader
    
    val rows: Iterator[Map[String,String]] = CSVDictReader("filename.csv")
    
### Configuring reader settings (e.g., different delimeters, etc.)

    import com.bizo.mighty.csv.{CSVReaderSettings, CSVReader, CSVDictReader}
    
    // change the default Reader Settings by modifying the separator to be ':'
    val settings = CSVReaderSettings.Standard.copy(separator = ':')

    val rows: Iterator[Array[String]] = CSVReader("filename.csv")(settings)
    
    val rowsWithHeaders: Iterator[Map[String,String]] = CSVDictReader("filename.csv")(settings)


Writing CSV Files
----------
### Writing csv rows without headers:
The CSVWriter.write method can take in either a Seq[String] or Array[String]
    import com.bizo.mighty.csv.CSVWriter
    
    val output: CSVWriter = CSVWriter("output.csv")
    
    output.write(Seq("row1,1", "row1,2")
    output.write(Array("row2,1", "row2,2")
    output.close()
    
    /**
     * writes the following to output.csv:
     * "row1,1","row1,2"
     * "row2,1","row2,2"
     */
    
### Writing csv rows with headers:
    import com.bizo.mighty.csv.CSVDictWriter
    
    val output: CSVDictWriter = CSVDictWriter("output.csv", Seq("col1", "col2"))
    
    // writes header
    output.writeHeader()
    
    // writes rows
    output.write(Map("col1"->"row1,1", "col2"->"row1,2"))
    output.write(Map("col1"->"row2,1", "col2"->"row2,2"))
    
    /**
     * writes the following to output.csv:
     * "col1","col2"
     * "row1,1","row1,2"
     * "row2,1","row2,2"
     */
     
### Configuring writer settings

    import com.bizo.mighty.csv.{CSVWriterSettings, CSVWriter, CSVDictWriter}
    
    // change the default writer Settings by modifying the separator to be ':'
    val settings = CSVReaderSettings.Standard.copy(separator = ':')

    val writer = CSVWriter("filename.csv")(settings)
    
    val dictWriter = CSVWriterReader("filename.csv")(settings)     
