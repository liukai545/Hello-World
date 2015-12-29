
package test.spark.firstApp

import java.io.FileWriter
import java.io.File
import java.util.Random
;

/**
 * Created by liukai on 2015/9/29.
 */
object SampleDataFileGenerator {
  def main(args:Array[String]): Unit ={
    val writer = new FileWriter(new File("d://peopleage"))

    val random = new Random()
    for(i <- 1 to 10000){
      writer.write(i + " " + random.nextInt(100) + "\n");
    }
    writer.flush();
    writer.close();
  }

}

