package reviewscollector

trait ISBNService {

  def resultForISBN(isbn: String): (String, Int)

}
