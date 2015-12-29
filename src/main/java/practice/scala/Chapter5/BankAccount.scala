package practice.scala.Chapter5

/**
  * Created by liukai on 2015/11/6.
  */
class BankAccount {

  val balance = 0

  def deposit() {}

  def withdraw() {}
}

object BankAccount{
  def main(args: Array[String]) {
    val account = new BankAccount()

    println(account.balance)
  }
}
