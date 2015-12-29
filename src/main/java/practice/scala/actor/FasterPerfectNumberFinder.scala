package practice.scala.actor

import scala.actors.Actor._

/**
  * Created by liukai on 2015/11/17.
  */
class FasterPerfectNumberFinder {
  def sumOfFactorsInRange(lower: Int, upper: Int, number: Int): Int = {
    //     flatMap 简写 /:
    val i1: Int = (0 /: (lower to upper)) { (sum, i) => if (number % i == 0) {
      sum + i
    } else sum
    }
    i1
  }

  def isPerfectConcurrent(candidate: Int) = {
    val RANGE = 10000000
    val numberOfPartitions = (candidate.toDouble / RANGE).ceil.toInt
    val caller = self

    for (i <- 0 until numberOfPartitions) {
      val lower = i * RANGE + 1
      val upper = candidate min (i + 1) * RANGE
      actor {
        caller ! sumOfFactorsInRange(lower, upper, candidate)
      }
    }

    val sum = (0 /: (0 until numberOfPartitions)) { (partialSum, i) =>
      receive {
        case sumInRange: Int => println("int" + sumInRange); partialSum + sumInRange
        case sumInRange: String => println("str" + sumInRange); partialSum + sumInRange.toInt
        case sumInRange: Double => println("double" + sumInRange); partialSum + sumInRange.toInt
        case _ => println(i + "other"); 3
      }
    }

    sum == 2 * candidate
  }
}

object FasterPerfectNumberFinder {
  def main(args: Array[String]) {
    val finder: FasterPerfectNumberFinder = new FasterPerfectNumberFinder
    println("335545036:" + finder.isPerfectConcurrent(335545036))
  }
}
