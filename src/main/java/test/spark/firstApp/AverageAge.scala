package test.spark.firstApp

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by liukai on 2015/9/29.
 */
object AverageAge {
  def main(args: Array[String]) {
    if (args.length < 1) {
      println("输入路径先")
    }

    val conf = new SparkConf().setAppName("average age")
    val sc = new SparkContext(conf)

    val textRDD = sc.textFile(args(0));

    val count = textRDD.count();

    val result = textRDD.map(line => Integer.parseInt(line.split(" ")(1))).reduce((a, b) => a + b)

    println("人数为： " + count)
    println("平均年龄为: " + (result / count))

  }

}

