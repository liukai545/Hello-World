package practice.scala.implicitt

import java.util.{Calendar, Date}

/**
 * Created by liukai on 2015/10/13.
 */
class DateHelper(number: Int) {
  def days(when: String): Date = {
    var date = Calendar.getInstance()

    when match {
      case "age" => date.add(Calendar.DAY_OF_MONTH, -number)
      case "from_now" => date.add(Calendar.DAY_OF_MONTH, number)
      case _ => date
    }
    date.getTime
  }
}

object DateHelper {
  val ago = "age"
  val from_now = "from_now"

  implicit def convertInt2DateHelper(number: Int) = new DateHelper(number)

}