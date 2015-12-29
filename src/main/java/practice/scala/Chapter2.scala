package practice.scala

import scala.collection.immutable.StringOps

/**
 * Created by liukai on 2015/10/31.
 */
class Chapter2 {

}

object Section1 {
  def main(args: Array[String]) {
    val num = 1
    val result = plusOrMinus(num)
    print(result)
  }

  def plusOrMinus(num: Int): Int = {
    var result = 0
    if (num > 0) {
      result = 1
    } else if (num < 0) {
      result = -1
    }
    result
  }
}

object Section4 {
  def main(args: Array[String]) {
    for (i <- (0 to 10).reverse) {
      println(i)
    }
  }
}

object Section5 {
  def main(args: Array[String]) {
    countdown(9)
  }

  def countdown(n: Int): Unit = {
    for (i <- 0 to n; j = n - i) {
      println(j)
    }
  }
}

object Section6 {
  def main(args: Array[String]) {
    val str = "Hello"
    var result = BigInt(1);

    for (i <- 0 until str.length) {
      result *= BigInt(scala.Array(str(i).toByte))
    }
    print(result)
  }
}


object Section8 {
  def product(str: String): BigInt = {
    var result = BigInt(1);
    for (i <- 0 until str.length) {
      result *= BigInt(scala.Array(str(i).toByte))
    }
    result
  }

  def main(args: Array[String]) {
    print(product("Hello"))
  }
}

object Section9 {

  def product(str: String, index: Int): BigInt = {
    if (index == str.length) {
      BigInt(1)
    } else {
      product(str, index + 1) * BigInt(scala.Array(str(index).toByte))
    }
  }

  def main(args: Array[String]) {
    var result = product("Hello", 0)
    print(result)
  }
}

object Section10 {
  def fun(x: Double, n: Int): Double = {
    if (n > 0 && n % 2 == 0) {
      math.pow(math.pow(x, n / 2), 2)
    } else if (n > 0 && n % 2 == 1) {
      x * math.pow(x, n - 1)
    } else if (n == 0) {
      1.0
    } else {
      1 / math.pow(x, -n)
    }
  }

  def main(args: Array[String]) {
    print(fun(2, -3))
  }
}
