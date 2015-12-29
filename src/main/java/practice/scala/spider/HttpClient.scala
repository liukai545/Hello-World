package practice.scala.spider

import java.io.{ByteArrayOutputStream, InputStream}
import java.net.{ConnectException, HttpURLConnection, URL}
import java.nio.charset.Charset
import org.apache.commons.io.IOUtils

import scala.collection.JavaConversions.asScalaBuffer

/**
  * Created by liukai on 2015/11/11.
  */
object HttpClient {

  val HTML_TYPE = "text/html"
  val CONTENT_TYPE = "Content-Type"

  implicit def urlStr2URL(url: String) = new URL(url)

  /**
    * 获取网页的内容
    * @param url
    */
  def getContent(url: String) = {
    val connection = url.openConnection().asInstanceOf[HttpURLConnection]

    val responseCode = connection.getResponseCode
    val charsetAndMine = getResponseCharsetAndMime(connection.getContentType)
    val inputstrean: InputStream = connection.getInputStream
    val data: Array[Byte] = IOUtils.toByteArray(inputstrean)
    //val content = getResponseContent(charsetAndMine._1, array)
    getResponseContent(charsetAndMine._1, data)
  }

  def getResponseContent(charset: String, data: Array[Byte]) = {
    if (charset.length == 0) {
      new String(data, Charset.forName("GB2312"))
    } else {
      new String(data, Charset.forName(charset))
    }
  }

  def getResponseCharsetAndMime(contentType: String) = {
    var charset = ""
    var mine = ""
    if (contentType.indexOf(";") != -1) {
      mine = contentType.substring(0, contentType.indexOf(";"))
      for (str <- contentType.split(";")) {
        if (str.trim().toLowerCase.startsWith("charset")) {
          charset = str.substring(str.indexOf("=") + 1).trim()
        }
      }
    } else {
      mine = contentType
    }
    (charset, mine)
  }

  def main(args: Array[String]) {
    val content: String = getContent("http://www.zhihu.com/question/21391305/answer/44542857")

    println(content)
  }
}