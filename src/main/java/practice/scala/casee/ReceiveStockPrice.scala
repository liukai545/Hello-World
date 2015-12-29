package practice.scala.casee

/**
 * Created by liukai on 2015/10/14.
 */
object ReceiveStockPrice {
  def unapply(input: String): Option[(String, Double)] = {
    try {
      if (input.contains(":")) {
        val splitQuote = input split ":"
        Some(splitQuote(0), splitQuote(1).toDouble)
      } else {
        None
      }
    } catch {
      case _: NumberFormatException => None
    }
  }
}
