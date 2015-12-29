package practice.scala.actor

import scala.actors.Actor
import scala.actors.Actor._

/**
  * Created by liukai on 2015/11/26.
  */
object CountingSample extends App{

  private val save: Actor = actor {
    while (true) {
      receive {
        case msg: String => println(msg)
      }
    }
  }

  new Thread(new Runnable {
    override def run(): Unit = {
      println("已发送")
      save ! "xxx"
    }
  }).start()

}