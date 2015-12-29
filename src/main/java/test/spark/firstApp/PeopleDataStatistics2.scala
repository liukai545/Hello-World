
package test.spark.firstApp

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.{LoggerFactory, Logger}

/**
  * Created by liukai on 2015/10/9.
  */
object PeopleDataStatistics2 {

  val logger = LoggerFactory.getLogger("peopleData")

   private val schemaString = "id,gender,height";

   def main(args: Array[String]): Unit = {
     if (args.length < 1) {
       logger.info("路径")
       System.exit(1)
     }

     val conf = new SparkConf().setAppName("people Data Statistics use sql")
     val sc = new SparkContext(conf)
     val peopleDataRDD = sc.textFile(args(0))
     val sqlCtx = new SQLContext(sc)

     import sqlCtx.implicits._

     val schemaArray = schemaString.split(",")
     val schema = StructType(schemaArray.map(fieldName => StructField(fieldName, StringType, true)))

     val rowRDD: RDD[Row] = peopleDataRDD.map(_.split(" ")).map(eachRow => Row(eachRow(0), eachRow(1), eachRow(2)))

     val peopleDF = sqlCtx.createDataFrame(rowRDD, schema)

     peopleDF.registerTempTable("people")

     //get the male people whose height is more than 180
     val higherMale180 = sqlCtx.sql("select id,gender, height from people where height > 180 and gender = 'M' ")
     logger.info("Men whose height are more than 180: " + higherMale180.count())
     logger.info("<Display #1>")
     //get the female people whose height is more than 170
     val higherFemale170 = sqlCtx.sql("select id,gender, height from people where height > 170 and gender = 'F' ")
     logger.info("Women whose height are more than 170: " + higherFemale170.count())
     logger.info("<Display #2>")
     //Grouped the people by gender and count the number
     peopleDF.groupBy(peopleDF("gender")).count().show()
     logger.info("People Count Grouped By Gender")
     logger.info("<Display #3>")
     //
     peopleDF.filter(peopleDF("gender").equalTo("M")).filter(
       peopleDF("height") > 210).show(50)
     logger.info("Men whose height is more than 210")
     logger.info("<Display #4>")
     //
     peopleDF.sort($"height".desc).take(50).foreach { row => logger.info(row(0) + "," + row(1) + "," + row(2)) }
     logger.info("Sorted the people by height in descend order,Show top 50 people")
     logger.info("<Display #5>")
     //
     peopleDF.filter(peopleDF("gender").equalTo("M")).agg(Map("height" -> "avg")).show()
     logger.info("The Average height for Men")
     logger.info("<Display #6>")
     //
     peopleDF.filter(peopleDF("gender").equalTo("F")).agg("height" -> "max").show()
     logger.info("The Max height for Women:")
     logger.info("<Display #7>")
     //......
     logger.info("All the statistics actions are finished on structured People data.")

   }

 }

