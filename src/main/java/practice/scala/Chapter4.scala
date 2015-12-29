package practice.scala

import java.util.Scanner

import scala.collection.immutable.IndexedSeq
import scala.collection.mutable

/**
  * Created by liukai on 2015/11/4.
  */
class Chapter4 {

}

object Section41 {
  def main(args: Array[String]) {
    val price: Map[String, Int] = Map("xxx" -> 1, "yyy" -> 2, "zzz" -> 3)

    val price2 = for ((k, v) <- price) yield {
      (k, v * 0.9)
    }

    price2.foreach(p => println(p._1 + "" + p._2))
  }
}

object Section42_4 {
  def main(args: Array[String]) {

    val in = new Scanner(new java.io.File("C:\\Users\\liukai\\Desktop\\WTF.txt"))

    val map = mutable.Map[String, Int]()

    while (in.hasNext) {
      val split: Array[String] = in.next().split(" ")
      for (word <- split) {
        if (map.contains(word)) {
          map(word) += 1
        } else {
          map(word) = 1
        }
      }
    }
    for ((k, v) <- map) {
      println(k + "  " + v)
    }
  }
}

object Section43_4 {
  def main(args: Array[String]) {

    val in = new Scanner(new java.io.File("C:\\Users\\liukai\\Desktop\\WTF.txt"))

    var map = scala.collection.immutable.SortedMap[String, Int]()

    while (in.hasNext) {
      val split: Array[String] = in.next().split(" ")
      for (word <- split) {
        if (map.contains(word)) {
          val num = map(word)
          map -= word
          map = map + (word -> (num + 1))
        } else {
          map = map + (word -> 1)
        }
      }
    }
    for ((k, v) <- map) {
      println(k + "  " + v)
    }
  }
}

import scala.collection.JavaConversions.mapAsScalaMap

object Section45 {
  def main(args: Array[String]) {

    val in = new Scanner(new java.io.File("C:\\Users\\liukai\\Desktop\\WTF.txt"))

    var map: scala.collection.mutable.Map[String, Int] = new java.util.TreeMap[String, Int]();

    while (in.hasNext) {
      val split: Array[String] = in.next().split(" ")
      for (word <- split) {
        if (map.contains(word)) {
          val num = map(word)
          map -= word
          map = map + (word -> (num + 1))
        } else {
          map = map + (word -> 1)
        }
      }
    }
    for ((k, v) <- map) {
      println(k + "  " + v)
    }
  }
}

object Section46 {
  def main(args: Array[String]): Unit = {
    val days = mutable.LinkedHashMap[String, Int]()

    days += ("Monday" -> java.util.Calendar.MONDAY)
    days += ("Tuesday" -> java.util.Calendar.TUESDAY)
    days += ("Wendensday" -> java.util.Calendar.WEDNESDAY)

    for ((k, v) <- days) {
      println(k + " " + v)
    }
  }
}

import scala.collection.JavaConversions.propertiesAsScalaMap

object Section47 {
  def main(args: Array[String]): Unit = {
    val map: scala.collection.Map[String, String] = System.getProperties

    val longestLength: Int = map.keySet.foldLeft("")((longest, str) => if (longest.length < str.length) {
      str
    } else {
      longest
    }).length

    for ((k, v) <- map) {
      println(k + " " * (longestLength - k.length) + " | " + v)
    }
  }
}

object Section48 {
  def main(args: Array[String]): Unit = {
    val ints: Array[Int] = Array(1, 2, 5, 3, 8)

    val max: (Int, Int) = minMax(ints)

    println(max._1)
    println(max._2)

  }

  def minMax(values: Array[Int]) = {
    val little = values.foldLeft(values(0))((little, i) => if (little < i) little else i)
    val large = values.foldLeft(values(0))((little, i) => if (little > i) little else i)

    (little, large)
  }
}

object Section49 {
  def main(args: Array[String]): Unit = {
    val ints: Array[Int] = Array(1, 2, -2, 0, 0, 3, 8)

    val lteqgt1: (Int, Int, Int) = lteqgt(ints, 0)
    println(lteqgt1.toString())

    val zip: IndexedSeq[(Char, Char)] = "hello".zip("worldefg")

    println(zip.mkString(","))

  }

  def lteqgt(values: Array[Int], v: Int) = {

    var xiaoyu: Int = 0
    var dengyu: Int = 0
    var dayu: Int = 0

    for (i <- values) {
      if (i > v) {
        dayu += 1
      } else if (i < v) {
        xiaoyu += 1;
      } else {
        dengyu += 1;
      }
    }
    (xiaoyu, dengyu, dayu)
  }
}