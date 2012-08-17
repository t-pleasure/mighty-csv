package com.bizo.mighty

package object csv {
  type Row = Array[String]
  type RowGroup[K] = Tuple2[K, Seq[Row]]
}