package practice.scala.spider.dao

import practice.scala.spider.dao.RepositoryCategory.RepositoryCategory

/**
  * Created by liukai on 2015/11/16.
  */
object RepositoryFactory {

  def createRepository(category: RepositoryCategory) = {
    category match {
      case RepositoryCategory.FILE => new CSVRepository()
    }
  }
}

object RepositoryCategory extends Enumeration {
  type RepositoryCategory = Value
  val FILE, HCATALOG, SPARKSQL = Value
}