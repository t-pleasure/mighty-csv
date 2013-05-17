mighty-csv
==========

mighty-csv is a csv utility for scala. Currently, it acts as a scala wrapper around the OpenCSV project (http://opencsv.sourceforge.net/). 

Benefits of using mighty-csv (as opposed to just OpenCSV):
----------
* Reader implments Iterator. This means that we can call foreach, map, fold, etc...
* Allows for automatic binding of rows to classes
* Able to associate rows with header information (e.g., a Dict Reader) 
* Able to write Maps as rows

SBT
----------
Tell sbt about a dependency on Mighty-CSV by adding a library dependency to your build.sbt file (or a Scala build file).
If you are using Scala 2.9, add the following:

    libraryDependencies += "com.bizo" % "mighty-csv_2.9.1" % "0.2"
    
If you are using Scala 2.10, add the following:    

    libraryDependencies += "com.bizo" % "mighty-csv_2.10.1" % "0.2"


Reading CSV Files (With Headers)
----------
### Read in a csv rows as Map[String, String]

    import com.bizo.mighty.csv.CSVDictReader
    
    val rows: Iterator[Map[String,String]] = CSVDictReader("filename.csv")
    
### Reading non-standard csv files (e.g., different delimeters, etc.)
To read in non-standard csv files, simply specify the appropriate OpenCSV CSVReader.

    import au.com.bytecode.opencsv.{ CSVReader => OpenCSVReader }
    import java.io.{InputStreamReader, FileInputStream}
    import com.bizo.mighty.csv.CSVDictReader
    
    // note: specify csv settings in OpenCSVReader
    val reader: OpenCSVReader = new OpenCSVReader(new InputStreamReader(new FileInputStream(fname), "UTF-8"))
    
    val rows: Iterator[Map[String,String]] = CSVDictReader(reader)

Reading CSV Files (No Headers)
----------
### Read in csv rows as Array[String]

    import com.bizo.mighty.csv.CSVReader
    
    val rows: Iterator[Array[String]] = CSVReader("filename.csv")

### Automatic Binding (reads in rows and maps row to class)
To Automatically bind a row to class, the class must contain a constructor that has the same number of arguments as the number of columns AND accepts only String arguments.
Reading in csv rows and binding them to a class:

    import com.bizo.mighty.csv.CSVReader  
    
    // case class that takes in two strings
    case class TwoColumnRow(col1: String, col2:String)
    
    // read in file that contains two columns and binds rows to TwoColumnRow
    val rows: Iterator[TwoColumnRow] = CSVReader.readAs[TwoColumnRow]("two_column.csv")


### Manually map rows to classes. 
   
    /** 
     * What person.csv might look like
     * --------- 
     * Tony, 21
     * Al, 50
     * Kip, 60
     * Zach, 50
     * ---------
     */
     
    import com.bizo.mighty.csv.CSVReader
    
    case class Person(name: String, age: Int)
    
    val rows: Iterator[Person] = CSVReader("people.csv") { case Array(name, age) =>
      Person(name, age.toInt)
    }




### Reading non-standard csv files (e.g., different delimeters, etc.)
To read in non-standard csv files, simply specify the appropriate OpenCSV CSVReader.

    import au.com.bytecode.opencsv.{ CSVReader => OpenCSVReader }
    import java.io.{InputStreamReader, FileInputStream}
    import com.bizo.mighty.csv.CSVReader
    
    // note: specify csv settings in OpenCSVReader
    val reader: OpenCSVReader = new OpenCSVReader(new InputStreamReader(new FileInputStream(fname), "UTF-8"))
    
    val rows: Iterator[Array[String]] = CSVReader(reader)
    
    
Writing CSV Files
----------
### Writing standard csv files:

    import com.bizo.mighty.csv.CSVWriter
    
    val output: CSVWriter = CSVWriter("output.csv")
    
### Writing non-standard csv files:

    import java.io._
    import au.com.bytecode.opencsv.{ CSVWriter => OpenCSVWriter }
    
    // note: specify csv settings in OpenCSVWriter
    val writer = new OpenCSVWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname), "UTF-8")))
    
    val output: CSVWriter = CSVWriter(writer)
