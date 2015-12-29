package practice.scala.Chapter5

/**
  * Created by liukai on 2015/11/6.
  */
object TrafficLightColor extends Enumeration {
  val Yellow, Green = Value
  val Red = Value(0, "stop")

  //引用枚举
  TrafficLightColor.Green

}
