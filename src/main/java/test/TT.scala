package test

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by liukai on 2015/12/9.
  */
object TT {
  def main(args: Array[String]) {
    val aToString: mutable.Map[String, String] = mutable.Map[String, String]()

    println(aToString.size)
    aToString += "str" -> "value"

    println(aToString.size)
    val ints: ArrayBuffer[Int] = ArrayBuffer[Int](3)

    println(ints.mkString(","))
  }
}
