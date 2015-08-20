package reviewscollector

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.NumberFormat
import java.util.Locale

class AmazonHTMLParser extends ISBNService {

  protected[reviewscollector] def convertURLToDocument(url: String): Document = {
    val response = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).execute()
    if (response.statusCode != 200)
      throw new RuntimeException(response.statusCode + ": " + response.statusMessage())
    else
      response.parse()
  }

  protected[reviewscollector] def extractBookTitleFromDocument(document: Document) = document.select("span#productTitle").text()

  protected[reviewscollector] def extractReviewCountFromDocument(document: Document) = {
    try {
      NumberFormat.getNumberInstance(Locale.US)
        .parse(document.select("a.a-link-normal.a-text-normal.product-reviews-link").text())
        .intValue
    } catch {
      case e: Exception => 0
    }
  }

  def resultForISBN(isbn: String) = {
    val documentFromURL = convertURLToDocument(s"http://www.amazon.com/exec/obidos/ASIN/$isbn")
    (extractBookTitleFromDocument(documentFromURL), extractReviewCountFromDocument(documentFromURL))
  }

}

