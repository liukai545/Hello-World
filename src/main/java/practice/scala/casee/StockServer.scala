package practice.scala.casee

/**
 * Created by liukai on 2015/10/14.
 */
object StockServer {
  def process(input: String) {
    input match {
      case Symbol() => println("Look up price for valid symbol " + input)

      //@在传递symbol前截住，先使用symbol
      case ReceiveStockPrice(symbol@Symbol(), price) => printf("Received price %f for symbol %s\n", price, symbol)
      case _ => println("Invalid input " + input)
    }
  }
}
