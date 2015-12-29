package practice.scala.traitt

/**
 * Created by liukai on 2015/10/12.
 */
trait UpperCaseWriter extends Writer {
  abstract override def writeMessage(message: String) = {
    super.writeMessage(message.toUpperCase())
  }
}
