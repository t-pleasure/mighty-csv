package com.bizo.mighty.collection
import annotation.tailrec

class ConsecutivelyGroupable[T](itor: Iterator[T]) {
  def consecutiveGrouping[K](extractKeyFn: T => K): Iterable[(K, Seq[T])] = new Iterable[(K, Seq[T])] {
    
    def extractKeyAndRow(row: T): (K, T) = (extractKeyFn(row), row)

    def iterator = new Iterator[(K, Seq[T])] {
      // initialize curKey and curRows
      var (curKey, curRows) = {
        if (itor.hasNext){
          val firstRow = itor.next()
          (extractKeyFn(firstRow), Seq(firstRow))
        } else {
          (null.asInstanceOf[K], null.asInstanceOf[Seq[T]])
        }
      }

      override def hasNext() = { itor.hasNext || curRows.nonEmpty }

      // returns collected rows, nextKey, and nextRows
      @tailrec def collect(key: K, lst: Seq[T]): (Seq[T], K, Seq[T]) = {
        if (itor.hasNext) {
          extractKeyAndRow(itor.next()) match {
            case (kk, row) if kk.equals(key) => {
              collect(key, lst ++ Seq(row))
            }
            case (kk, row) => (lst, kk, Seq(row))
          }
        } else {
          (lst, null.asInstanceOf[K], Seq())
        }
      }
      
      override def next() = {
        if(curKey == null){sys.error("current key not defined")}
        val key = curKey
        val (collectedRows, nextKey, nextRows) = collect(key, curRows)
        curKey = nextKey
        curRows = nextRows
        (key, collectedRows)
      }
    }
  }
}

object ConsecutivelyGroupable {
  implicit def iteratorToGC[T](itor: Iterator[T]) = new ConsecutivelyGroupable[T](itor)
}
