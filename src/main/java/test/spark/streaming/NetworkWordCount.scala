package test.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by liukai on 2015/12/7.
  */
class NetworkWordCount {
  def run(): Unit = {
    val conf = new SparkConf().setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(1))

    val textStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 8123)

    val words: DStream[String] = textStream.flatMap(str => str.split(" "))

    val pairs: DStream[(String, Int)] = words.map(word => (word, 1))
    val wordCounts: DStream[(String, Int)] = pairs.reduceByKey(_ + _)

    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()
  }
}

object NetworkWordCount {
  def main(args: Array[String]) {
    new NetworkWordCount().run()
  }
}
