package practice.scala.spider

import scala.util.matching.Regex

/**
  * Created by liukai on 2015/11/13.
  */
trait Extrator {
  def extrat(extratRule: ExtratRule, content: String) = {
    val reg: Regex = extratRule.pattern.r
    for (reg(item) <- reg.findAllIn(content) if extratRule.filter(item)) yield {
      item
    }
  }
}

