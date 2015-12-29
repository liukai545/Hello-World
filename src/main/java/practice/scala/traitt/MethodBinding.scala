package practice.scala.traitt

/**
 * Created by liukai on 2015/10/12.
 */
object MethodBinding {

  //最右的

  def main(args: Array[String]) {
    val myWriterProfanityFirst = new StringWriterDelegate with UpperCaseWriter with ProfanityFilteredWtiter

    val myWriterProfanityLast = new StringWriterDelegate with ProfanityFilteredWtiter with UpperCaseWriter

    myWriterProfanityFirst writeMessage "There is no sin except stupidity"

    myWriterProfanityLast writeMessage "There is no sin except stupidity"

    println(myWriterProfanityFirst)

    println(myWriterProfanityLast)
  }
}
