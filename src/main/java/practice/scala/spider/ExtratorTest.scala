package practice.scala.spider

import java.io._
import java.net.URL
import java.util
import java.util.concurrent.LinkedBlockingQueue
import com.sun.mail.iap.ConnectionException

import scala.collection.mutable
import scala.io.{Codec, Source}
import scala.util.matching.Regex

/**
  * Created by liukai on 2015/11/10.
  */
class ExtratorTest(startURL: String) {
  val root = "D:\\we"
  val labelahref = "<a[\\s]+href[\\s]*=[\\s]*\"([^<\"]+)\""
  val linksQueue = new LinkedBlockingQueue[String]()
  linksQueue.add(startURL)
  val visited = new util.LinkedList[String]()


  def crawl(): Unit = {

    var count = 10
    while (linksQueue.size() > 0 && count > 0) {
      val link: String = getNextLink()
      println(count + "       ", link)
      try {
        val content = HttpClient.getContent(link) //downer

        /*storageSite(link, content._4) //storage
        visited.add(link)
        if (content._3.trim() == HttpClient.HTML_TYPE) {
          val urls: Array[String] = extratHttpUrls(labelahref, HttpClient.getResponseContent(content._2, content._4))
          urls.foreach(linksQueue.put(_))
        }
        count -= 1
        Thread.sleep(5000)*/
      }catch{
        case ex: ConnectionException => visited.add(link)
      }
    }

    def getNextLink(): String = {
      val poll: String = linksQueue.poll()
      if (visited.contains(poll)) {
        getNextLink()
      } else {
        poll
      }
    }

    /**
      * 从网页中解析链接
      */
    def produceLinksToCrawl(links: String): Unit = {

    }


    def storageSite(host: String, data: Array[Byte]): Unit = {
      val writer: PrintWriter = new PrintWriter("d:/web/" + new URL(host).getHost + ".html")
      val s: String = new Predef.String(data)
      writer.write(s)
      writer.flush()
      writer.close()
    }

    /**
      * 提取外链
      * @param pattern
      * @param content
      * @return
      */
    def extratHttpUrls(pattern: String, content: String) = {
      val hrefPattern: Regex = pattern.r
      val strings: Iterator[String] = for (hrefPattern(item) <- hrefPattern.findAllIn(content)) yield {
        item
      }
      strings.toArray.filter(str => !(str.startsWith("java") || str.startsWith("#") || str.contains("+"))).distinct
    }
  }
}

object ExtratorTest {
  def main(args: Array[String]): Unit = {
    val url = "http://www.sohu.com"
    val extrator = new ExtratorTest(url)
    extrator.crawl()


  }
}
