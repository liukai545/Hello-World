package test.spark.mllib.collaborativefiltering

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.recommendation.{ALS,Rating}
import org.apache.log4j.{Logger,Level}


/**
 * Created by liukai on 2015/10/17.
 */
object Prediction {

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Collaborative Filtering")
    val sc = new SparkContext(conf)

    //1::362::5::838984885
    val data = sc.textFile(args(0))
    val ratings = data.map(_.split("::") match {case Array(user,movie,rate,time) => Rating(user.toInt,movie.toInt,rate.toDouble)}).cache()

    println("评分数据格式举例：" + ratings.first())
    println("用户的数量："+ ratings.map(_.user).distinct())
    println("电影的数量：" + ratings.map(_.product).distinct())

    //划分训练集和测试集
    val splits = ratings.randomSplit(Array(0.8,.02),seed = 111)
    val training = splits(0).repartition(5)
    val test = splits(1).repartition(5)

    //训练
    val rank =12
    val lamda = 0.01
    val numIterations = 20
    val model = ALS.train(ratings,rank,numIterations,lamda)

    println("user features:" + model.userFeatures)
    println("user features count:" + model.userFeatures.count())
    println("product features:" + model.productFeatures)
    println("product features count:" + model.productFeatures.count())

    //评估
    val usersProducts = ratings.map{case Rating(user,product,rate) => (user,product)}
    var predictions = model.predict(usersProducts).map{case Rating(user,product,rate)=> ((user,product),rate)}

    val ratesAndPreds = ratings.map{case Rating(user,product,rate) =>((user,product),rate)}.join(predictions)

    //计算方差
    val rmse = math.sqrt(ratesAndPreds.map{case ((user,product),(r1,r2)) =>
        val err = r1-r2
        err*err
    }.mean())

    println(s"RMSE = $rmse")

    //为384用户推荐电影
    val K = 10
    val topKRecs = model.recommendProducts(384, K)
    println(topKRecs.mkString("\n"))

    //保存真实评分与预测评分
    ratesAndPreds.sortByKey().repartition(1).sortBy(_._1).map({
      case ((user,product),(rate,pred)) => (user + "," + product + "," + rate + "," + pred)
    }).saveAsTextFile("/tmp/result")

  }

}
