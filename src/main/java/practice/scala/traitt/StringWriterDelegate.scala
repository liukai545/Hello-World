package practice.scala.traitt

import java.io.StringWriter

/**
 * Created by liukai on 2015/10/12.
 */
class StringWriterDelegate extends Writer{

  val writer = new StringWriter()

  override def writeMessage(message: String){
    writer.write(message)
  }

  override def toString(): String ={
    writer.toString
  }
}
