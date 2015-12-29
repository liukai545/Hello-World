package practice.scala.spider

import java.net.URL

import practice.scala.spider.domain.{Topic, Question}

import scala.collection.{immutable, mutable}

/**
  * Created by liukai on 2015/11/13.
  * QuestionProducer
  */
class QuestionLinkExtrator(topic: Topic) extends Extrator {

  def parseQuestionLinks() = {
    val questionExtratRule = ExtratRule(
      """href="([^"]*)"""", "href=\"/question/24708380\"", (url: String) =>
        url.contains("question/") && !url.contains("answer"))
    val content: String = HttpClient.getContent(topic.url)
    val links = new mutable.ListBuffer[String]()
    extrat(questionExtratRule, content).foreach(link => links += ("http://" + new URL(topic.url).getHost + link))
    links
  }
}
