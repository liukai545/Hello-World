package test.spark.sparksql

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.{Row, DataFrame}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by liukai on 2015/11/9.
  */
object SQLMLlib {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("SQLMLlib")
    println(System.getProperty("xxx") + "********************************************")
    val sc = new SparkContext(conf)
    val hiveContext = new HiveContext(sc);

    hiveContext.sql("use saledata")
    hiveContext.sql("set spark.sql.shuffle.partitions = 20")
    val sqldata: DataFrame = hiveContext.sql("select a.locationid,sum(b.qty) totalqty,sum(b.amount) totalamount from tblStock a join tblstockdetail b on a.ordernumber=b" +
      ".ordernumber group by a.locationid")

    //查询结果转换成向量
    val parsedData = sqldata.map { case Row(_, totalqty, totalamount) =>
      val features = Array[Double](totalqty.toString.toDouble, totalamount.toString.toDouble)
      Vectors.dense(features)
    }

    //对数据进行聚类，3各类，20次迭代，形成数据模型

    val numClusters = 3
    val numIterations = 20

    val models = KMeans.train(parsedData,numClusters,numIterations)

    //分类

    sqldata.map{
      case Row(locationid,totalqty,totalamount) =>
        val features = Array[Double](totalqty.toString.toDouble,totalamount.toString.toDouble)
        val linevectore = Vectors.dense(features)
        val prediction = models.predict(linevectore)

        locationid + " " + totalqty + " " + totalamount + " " + prediction
    }.saveAsTextFile("/user/liukai/data/resluts")

    sc.stop()

  }
}
