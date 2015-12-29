package practice.scala.spider

/**
  * Created by liukai on 2015/11/13.
  */
case class ExtratRule(pattern: String, description: String, filter: (String => Boolean) = (url: String) => true) {
}
