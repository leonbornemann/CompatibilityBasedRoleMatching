package de.hpi.dataset_versioning.data.change

import java.time.LocalDate

import scala.collection.mutable.ArrayBuffer

//begin inclusive, end exclusive
case class TimeInterval(begin:LocalDate,end:Option[LocalDate]) {
  def subIntervalOf(other: TimeInterval) = {
    !begin.isBefore(other.begin) && !end.getOrElse(LocalDate.MAX).isAfter(other.end.getOrElse(LocalDate.MAX))
  }

  def endOrMax = end.getOrElse(LocalDate.MAX)

}

object TimeInterval {

  def notIncludedIN(sortedIntervalsA: ArrayBuffer[TimeInterval], sortedIntervalsB: ArrayBuffer[TimeInterval]): Boolean = {
    val itA = sortedIntervalsA.iterator
    val itB = sortedIntervalsB.iterator
    var included = true
    var curElemB = itB.next()
    while(included && itA.hasNext){
      var curElemA = itA.next()
      while(!curElemA.begin.isBefore(curElemB.endOrMax) && itB.hasNext){
        //the interval is too early
        curElemB = itB.next()
      }
      included = curElemA.subIntervalOf(curElemB)
    }
    included
  }
}
