
package test.spark.firstApp

import java.io.FileWriter
import java.io.File;

import scala.util.Random

/**
 * Created by liukai on 2015/9/29.
 */
object PeopleInfoFileGenerator {
  def main(args: Array[String]): Unit = {
    val random = new Random()

    val writer = new FileWriter(new File("d://peopleheight"))

    for (i <- 1 to 10000) {
      if (random.nextBoolean())
        writer.write(i + " " + "M " + random.nextInt(200) + "\n")
      else
        writer.write(i + " " + "F " + random.nextInt(200) + "\n")
    }
    writer.flush()
    writer.close()
  }
}

