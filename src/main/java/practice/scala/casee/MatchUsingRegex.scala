package practice.scala.casee

import scala.util.matching

/**
 * Created by liukai on 2015/10/14.
 */
object MatchUsingRegex {

  def process(input: String) {
    //val GoogStock = """^GOOG:(\d*\.\d+)""".r

    val MatchStock = """^(.+):(\d*\.\d+)""".r



    input match {
      //这个参数price用来是来接收提取器的值的，是传出不是传入
      //case GoogStock(price) => println("price of GOOG is " + price)
      case MatchStock("GOOG", price) => println("price of GOOG is " + price)
      case MatchStock(symbol, price) => printf("price of %s is %s\n", symbol, price)

      case _ => println("no processsing " + input)
    }
  }

  def main(args: Array[String]) {
    process("GOOG:23.23")
    process("GOOG:23")
    process("IBM:234.23")
  }
}
