mighty-csv
==========

mighty-csv is a csv utility for scala. Currently, it acts as a scala wrapper around the OpenCSV project (http://opencsv.sourceforge.net/). 

Benefits of using mighty-csv (as opposed to just OpenCSV):
----------
* Reader implments Iterator. This means that we can call foreach, map, fold, etc...
* Allows for automatic binding of rows to classes

Reading CSV Files
----------

To Automatically bind a row to class, the class must contain a constructor that has the same number of arguments as the number of columns AND accepts only String arguments.
Reading in csv rows and binding them to a class:

    import com.bizo.mighty.csv.CSVReader  
    
    // case class that takes in two strings
    case class TwoColumnRow(col1: String, col2:String)
    
    // read in file that contains two columns and binds rows to TwoColumnRow
    val rows: Iterator[TwoColumnRow] = CSVReader[TwoColumnRow]("two_column.csv")



To manually map rows to classes:
   
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
    
    val rows: Iterator[Person] = CSVReader.read("people.csv") { case Array(name, age) =>
      Person(name, age.toInt)
    }



To Read in csv rows as Array[String]

    import com.bizo.mighty.csv.CSVReader
    
    val rows: Iterator[Array[String]] = CSVReader.readAsRows("filename.csv")


