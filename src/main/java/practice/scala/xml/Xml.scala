package practice.scala.xml

import java.io.{ByteArrayInputStream, FileInputStream, File}
import java.net.URL
import java.nio.charset.Charset

import org.apache.commons.io.IOUtils

import scala.xml.{NodeSeq, XML}

/**
  * Created by liukai on 2015/11/18.
  */
class Xml {
  val doc = <html>
    <head>
      <title>baidu</title>
    </head>
  </html>
  val item = <li>fred</li> <li>vilma</li>;

  def load() = {
    val url = "http://www.zhihu.com/collection/75193784"

    val file: File = new File("D:\\www.qq.com.html")
    val in = new FileInputStream(file)

    val string: String = IOUtils.toString(in)

    val string1: String = string.replaceAll("&nbsp;"," ").replaceAll("&raquo;"," ").replaceAll("&bull;"," ")
      .split("\n|\r\n").filter(str => !str.contains("link") && !str.contains("meta") && !str.contains("input")&& !str.contains("textarea")).mkString(System.lineSeparator())


    //val root = XML.load(new ByteArrayInputStream(string1.getBytes()))

    val seq: NodeSeq = doc \ "head" \ "title"

    println(seq.text)
  }

}

object Xml {
  def main(args: Array[String]) {
    new Xml().load()
  }
}