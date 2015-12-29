
package test.spark.firstApp

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by liukai on 2015/9/29.
 */
object TopK {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("top key")

    val sc = new SparkContext(conf)

    val textFile = sc.textFile(args(0))

    val sorted = textFile.flatMap(line => line.toLowerCase.split(" ")).map(line => (line, 1)).reduceByKey((a, b) => a + b).map { case (k, v) => (v, k) }.sortByKey(false)

    val topk = sorted.take(5) map { case (k, v) => (v, k) }

    topk.foreach(println)
  }
}

