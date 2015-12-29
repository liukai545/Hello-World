
package test.spark.firstApp

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by liukai on 2015/9/29.
 */
object HelloSpark {
  def main(args: Array[String]) {

    if (args.length < 1) {
      println("输入路径先")
      System.exit(1)
    }
    println("hello world");
    val conf = new SparkConf().setAppName("hello");

    val sc = new SparkContext(conf);

    val textFile = sc.textFile(args(0))

    val wordcounts = textFile.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey((a, b) => a + b);

    wordcounts.saveAsTextFile("/user/cloudera/liukai/output")

    println("ok")

  }
}

