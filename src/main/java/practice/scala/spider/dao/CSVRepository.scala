package practice.scala.spider.dao

import java.io.FileOutputStream
import java.io.File

import practice.scala.spider.domain.Question

import scala.collection.mutable


/**
  * Created by liukai on 2015/11/17.
  */
class CSVRepository() extends Repository {
  private var writer: FileOutputStream = null

  def writer_(path: String) = {
    val file: File = new File(path)
    if (!file.getParentFile.exists()) {
      file.getParentFile.mkdir()
    }
    writer = new FileOutputStream(file)
  }

  def save(question: Question) = {
    val sb = new StringBuilder();
    sb.append(question.title).append(",")
    sb.append(question.url).append(",")
    sb.append(question.date).append(System.lineSeparator())
    val strings: mutable.Queue[String] = for (str <- question.answers) yield {
      str.url
    }

    //sb.append(strings.mkString("|")).append(",")
    writer.write(sb.toString().getBytes())
    writer.flush()
  }

  def close(): Unit = {
    writer.flush()
    writer.close()
  }
}
