package test.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
  * Created by liukai on 2015/12/9.
  */
object InvertedIndex {
  def main(args: Array[String]) {
    val source = args(0)

    val conf: SparkConf = new SparkConf().setAppName("inverted index")
    val sc: SparkContext = new SparkContext(conf)

    val file: RDD[String] = sc.textFile(source)
    val idToContentPair: RDD[(String, String)] = file.map(_.split(",")).map(arr => (arr(0), arr(1)))

    val dfInRecords: RDD[mutable.Map[String, Map[String, ArrayBuffer[Int]]]] = idToContentPair.map(pair => {
      val docId: String = pair._1
      val content: String = pair._2
      var pos = 0
      //val docId2Poses = mutable.Map[String, ArrayBuffer[Int]](docId -> ArrayBuffer[Int]())
      val dfInRecord = mutable.Map[String, Map[String, ArrayBuffer[Int]]]()
      content.split(" ").map(key => {
        if (dfInRecord.keySet.contains(key)) {
          dfInRecord(key).get(docId).get += pos
        } else {
          dfInRecord += key -> Map[String, ArrayBuffer[Int]](docId -> ArrayBuffer[Int](pos))
        }
        pos += key.length + 1
      }
      )
      dfInRecord
    })
    val map: RDD[(String, Map[String, ArrayBuffer[Int]])] = dfInRecords.flatMap(item => item.map(pair => (pair._1, pair._2)))

    val reslut: RDD[(String, Map[String, ArrayBuffer[Int]])] = map.reduceByKey(_ ++ _)

    reslut.collect().foreach(item => {
      val sb = new StringBuffer();
      sb.append("{\"" + item._1 + "\":[")
      item._2.foreach(docId2Poses => {
        sb.append("{\"" + docId2Poses._1 + "\":")
        sb.append(docId2Poses._2.mkString("[", ",", "]") + "},")
      }
      )
      sb.append("]}")
      println(sb.toString)
    })

    sc.stop();
  }
}
