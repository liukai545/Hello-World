package practice.scala.Chapter5

/**
  * Created by liukai on 2015/11/6.
  */
class Counter {
  private var value = 0;

  def increament() {

    if (value == Int.MaxValue) {
      value
    } else {
      value += 1
    }
  }

  def currentValue {
    value
  }
}
