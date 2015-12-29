
package test.spark.firstApp

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by liukai on 2015/9/29.
 */
object PeopleInfoCalculator {
  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("给个路径先")
      System.exit(1)
    }

    val conf = new SparkConf().setAppName("people height")

    val sc = new SparkContext(conf)

    val textFile = sc.textFile(args(0))

    val female = textFile.filter(line => line.contains("F"))
    val man = textFile.filter(line => line.contains("M"))

    println("男士人数为：" + man.count())
    println("女士人数为：" + female.count())

    val height = female.map(line => (line.split(" ")(2))).sortBy(height => height.toInt,false)

    val highfamale = height.first()

    println("女士最高为：" + highfamale)

  }

}

