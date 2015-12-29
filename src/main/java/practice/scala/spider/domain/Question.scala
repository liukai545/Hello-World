package practice.scala.spider.domain

import java.util.Date

/**
  * Created by liukai on 2015/11/13.
  */
class Question(var url: String) {
  var title: String = null
  var content: String = null
  val date: String = new Date().toLocaleString
  val answers: scala.collection.mutable.Queue[Answer] = new scala.collection.mutable.Queue[Answer]()
}

object Question {
  def apply(url: String) = {
    new Question(url)
  }
}
