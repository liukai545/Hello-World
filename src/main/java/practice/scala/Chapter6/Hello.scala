package practice.scala.Chapter6

import scala.collection.mutable

/**
  * Created by liukai on 2015/11/6.
  */
class Hello{
  println("hello")

  private val strings: mutable.SynchronizedQueue[String] = new mutable.SynchronizedQueue[String]()

  strings.++("xx")
  strings ++ "yy"

  strings.foreach(println)

}

object Hello{
  def main(args: Array[String]) {
    new Hello
  }
}
