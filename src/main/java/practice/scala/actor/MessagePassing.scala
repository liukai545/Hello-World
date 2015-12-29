package practice.scala.actor

import scala.actors.Actor._

/**
  * Created by liukai on 2015/11/17.
  */
class MessagePassing {
  def run(): Unit = {
    var startTime: Long = 0
    val caller = self
    val engrossedActor = actor {
      println("Number of message received so far?" + mailboxSize)
      caller ! "send"
      Thread.sleep(3000)
      println("Number of message received while i was busy?" + mailboxSize)
      receive {
        case msg =>
          val receivedTime = System.currentTimeMillis() - startTime
          println("Received message" + msg + " after " + receivedTime + " ms")
      }

      caller ! "received"
    }

    receive {
      case _ =>
    }

    println("sending Message")
    startTime = System.currentTimeMillis()

    engrossedActor ! "hello buddy"
    val endTime = System.currentTimeMillis() - startTime

    printf("Took less than %dms to send mossage\n", endTime)
    receive {
      case _ =>
    }


  }
}

object MessagePassing{
  def main(args: Array[String]) {
    new MessagePassing().run()
  }
}
