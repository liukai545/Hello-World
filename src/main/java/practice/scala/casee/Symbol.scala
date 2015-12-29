package practice.scala.casee

/**
 * Created by liukai on 2015/10/14.
 */
object Symbol {
  def unapply(symbol: String): Boolean = {
    symbol == "GOOG" || symbol == "IBM"
  }

  def main(args: Array[String]) {
    StockServer process "GOOG"
    StockServer process "GOOG:234.2"
    StockServer process "IBM:BUY"
    StockServer process "ERR:23.23"
  }
}
