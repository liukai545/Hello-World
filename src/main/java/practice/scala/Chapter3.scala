package practice.scala

import java.util

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created by liukai on 2015/10/31.
  */
class Chapter3 {

}

object Section31 {
  def main(args: Array[String]) {

    val n = 9
    val a = new Array[Int](n)
    for (i <- 0 until a.length) {
      a(i) = Random.nextInt(n);
    }

    print(a.mkString("x", ",", "x"))
  }
}

object Section32 {
  def main(args: Array[String]) {
    val a = Array(1, 2, 3, 4, 5)
    for (i <- 0 until(a.length, 2)) {
      if (i + 1 != a.length) {
        val tmp = a(i)
        a(i) = a(i + 1)
        a(i + 1) = tmp
      }
    }
    print(a.mkString("(", ",", ")"))
  }
}

object Section33 {
  def main(args: Array[String]) {
    val a = Array(1, 2, 3, 4, 5)
    val b = for (i <- 0 until (a.length)) yield {
      if (i % 2 == 0 && i + 1 != a.length) {
        a(i + 1)
      } else if (i % 2 == 1 && i + 1 != a.length) {
        a(i - 1)
      } else {
        a(i)
      }
    }
    print(b.mkString("(", ",", ")"))
  }
}

object Section34 {
  def main(args: Array[String]) {
    val a = Array(1, 2, 0, 3, -4, 5)

    val zhengshu = a.filter(p => p > 0)
    val feizhengshu = a.filter(p => p <= 0)

    val result = zhengshu ++ feizhengshu
    print(result.mkString("[", ", ", "]"))
  }
}

object Section35 {
  def main(args: Array[String]) {
    val doubles = new Array[BigDecimal](3)

    doubles(1) = 0.1
    doubles(0) = 0.2
    doubles(2) = 0.4

    print(doubles.mkString("x"))

    print(doubles.sum / doubles.length)
  }
}

object Section36_7 {
  def main(args: Array[String]) {
    val doubles = new Array[BigDecimal](4)

    doubles(0) = 0.2
    doubles(1) = 0.1
    doubles(2) = 0.4
    doubles(3) = 0.4

    println(doubles.reverse.mkString(","))

    println(doubles.distinct.mkString("  "))


    val doubless = new ArrayBuffer[BigDecimal]()

    doubless +=(1.1, 2.2, 3.3)

    for (i <- 0 until doubless.length / 2) {
      val t = doubless(i);
      doubless(i) = doubless(doubless.length - i - 1)
      doubless(doubless.length - i - 1) = t
    }

    print(doubless.mkString(","))

  }
}

object Section38 {
  def main(args: Array[String]) {
    val ds = ArrayBuffer[Int](1, 2, 3, 4, -1, -2, -3)

    val indexes = for (e <- 0 until ds.length if (ds(e) < 0)) yield {
      e
    }
    val idx = indexes.reverse.dropRight(1)

    for (i <- 0 until idx.length) {
      ds.remove(idx(i))
    }

    println("d昂钱")
    println(ds.mkString(","))
  }
}


object Section39 {
  def main(args: Array[String]): Unit = {
    val timezones = java.util.TimeZone.getAvailableIDs()

    /*    val result = timezones.map(str => if (str.startsWith("Asia")) str.substring("Asia".length)
        else {
          str
        })*/

    val result = for (i <- 0 until timezones.length) yield {
      if (timezones(i).startsWith("Asia")) timezones(i).substring("Asia".length)
      else {
        timezones(i)
      }
    }
    println(result.mkString("\n"))
    timezones.foreach(println)
  }
}

import java.awt.datatransfer._

object Section30 {
  val flavors = SystemFlavorMap.getDefaultFlavorMap.asInstanceOf[SystemFlavorMap]

  val flavor: util.List[String] = flavors.getNativesForFlavor(DataFlavor.imageFlavor)

  private val strings: ArrayBuffer[util.List[String]] = ArrayBuffer(flavor)

  println(strings.mkString(","))
}