package practice.scala.spider


import java.util.concurrent.Executors

import practice.scala.spider.domain.{Question, Topic}

import scala.collection.mutable.{ListBuffer, Queue}
import practice.scala.spider.dao.{CSVRepository, RepositoryCategory, RepositoryFactory}
import scala.actors.Actor._

/**
  * Created by liukai on 2015/11/13.
  */
class Crawlerr(topic: Topic) {

  def crawl(): Unit = {
    val questionLinks = new QuestionLinkExtrator(topic).parseQuestionLinks()
    val start = System.currentTimeMillis()

    val questions: ListBuffer[Question] = for (link <- questionLinks) yield {
      QuestionFromTopicExtrator(link).parseQuestionInfo()
    }

    /*    var caller = self
        for (question <- questions) {
          actor {
            caller ! QuestionInfoExtrator(question).setQuestionInfo()
          }
        }

        for (question <- questions) {
          receive { case _ => println("x")}
        }*/

    println(System.currentTimeMillis() - start + "time")

    val repository: CSVRepository = RepositoryFactory.createRepository(RepositoryCategory.FILE)
    repository.writer_("d://zhi/topic")
    questions.foreach(repository.save(_))
    repository.close()
  }
}

object Crawlerr {
  def main(args: Array[String]) {
    val topic = Topic("http://www.zhihu.com/topic/19551618")
    val crawlerr: Crawlerr = new Crawlerr(topic)
    crawlerr.crawl()
  }
}