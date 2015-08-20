import reviewscollector.ReviewsCollector
import reviewscollector.AmazonHTMLParser
import scala.io.Source

object MainProgram {

  def main(args: Array[String]): Unit = {
    val reviewsCollector = new ReviewsCollector
    val isbnService = new AmazonHTMLParser
    reviewsCollector.setISBNService(isbnService)
    val listOfISBN = Source.fromURL(getClass.getResource("/isbnlist.txt")).getLines().toList
    print(reviewsCollector.getResultForListOfISBN(listOfISBN))
  }

  def print(result: (List[(String, String, Int)], Int, List[(String, String)])) {
    println("Successful retrieval of review counts")
    println("--------------------------------------------------------------------------")
    result._1.foreach(x => println("ISBN: " + x._1 + ", Title: " + x._2 + ", ReviewCount: " + x._3))
    println("--------------------------------------------------------------------------")
    println("List of ISBN for which processing failed")
    result._3.foreach(x => println("ISBN: " + x._1 + ", Failure Reason: " + x._2))
    println("--------------------------------------------------------------------------")
    println("Total review count: " + result._2)
  }

}
