package reviewscollector

class ReviewsCollector {

  var service: ISBNService = _

  def getSortedListOfTuples(listOfResults: List[(String, String, Int)]) = listOfResults.sortBy(_._3).sortBy(_._2)

  def calculateTotalNumberOfReviews(listOfResults: List[(String, String, Int)]) = listOfResults.map(_._3).sum

  def setISBNService(isbnService: ISBNService) { service = isbnService }

  def getResultForISBN(isbn: String) = {
    try {
      val (title, numberOfReviews) = service.resultForISBN(isbn)
      (isbn, title, numberOfReviews, "")
    } catch {
      case e: RuntimeException => (isbn, "", 0, e.getMessage)
    }
  }

  def getResultForListOfISBN(isbnList: List[String]) = {
    val rawResult = isbnList.par.map(getResultForISBN)

    val titleAndNumberOfReviews = rawResult.filter(_._2 != "").map(data => (data._1, data._2, data._3)).toList
    val errors = rawResult.filter(_._4 != "").map(data => (data._1, data._4)).toList

    (getSortedListOfTuples(titleAndNumberOfReviews), calculateTotalNumberOfReviews(titleAndNumberOfReviews), errors)
  }


}
