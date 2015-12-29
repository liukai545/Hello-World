package practice.scala.spider


import practice.scala.spider.domain.{Answer, Question}
import scala.collection.mutable


/**
  * Created by liukai on 2015/11/14.
  */
class QuestionFromTopicExtrator(questionLink: String) extends Extrator {

  def parseQuestionInfo(): Question = {
    val answerLinkrule = ExtratRule(
      """href="([^"]*)"""", "href=\"/question/24708380/answer/2348102\"", (url: String) =>
        url.contains("question/") && url.contains("answer"))
    val content = HttpClient.getContent(questionLink)
    val question = Question(questionLink)
    question.content = content
    //设置answerLink
    extrat(answerLinkrule, content).foreach(link => question.answers.enqueue(Answer(link)))
    //设置title
    val titleRule = ExtratRule( """<title>([^"]*)</title>""", "标题")
    question.title = extrat(titleRule, content).map(str => str.substring(0, str.lastIndexOf("- 知乎") - 1).replaceAll(",|，", " ")).next().trim
    question
  }
}

object QuestionFromTopicExtrator {
  def apply(questionLink: String) = new QuestionFromTopicExtrator(questionLink)
}