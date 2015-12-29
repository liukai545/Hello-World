package practice.scala.implicitt

import DateHelper._

/**
 * Created by liukai on 2015/10/13.
 */
object Run {
  def main(args: Array[String]) {
    val past = 3 days ago

    val appointment = 4 days from_now

    println(past)
    println(appointment)

    val map = Map("key" -> "value")
    val filtermap = map filter { e => (e._1 contains "k") && (e._2 contains "v") }

    filtermap foreach { e => print(e.toString()) }

    val list = List("abc", "defgh", "igk")

    println(list.foldLeft(0)((sum, e) => sum + e.length))
  }
}
