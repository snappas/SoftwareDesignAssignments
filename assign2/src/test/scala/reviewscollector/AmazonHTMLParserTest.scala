package reviewscollector

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.BeforeAndAfter
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import scala.io.Source
import org.scalatest.Inside

@RunWith(classOf[JUnitRunner])
class AmazonHTMLParserTest extends FunSuite with Matchers with BeforeAndAfter with TableDrivenPropertyChecks with MockitoSugar {

  var amazonParser: AmazonHTMLParser = _
  var amazonParserSpy: AmazonHTMLParser = _

  val documentNoBookNoReview = Jsoup.parse(Source.fromFile(getClass.getResource("/sample1.html").getFile).mkString)
  val documentBookNoReview = Jsoup.parse(Source.fromFile(getClass.getResource("/sample2.html").getFile).mkString)
  val documentBookAndReview = Jsoup.parse(Source.fromFile(getClass.getResource("/sample3.html").getFile).mkString)
  val documentFromNoHtml = Jsoup.parse("")

  before {
    amazonParser = new AmazonHTMLParser
    amazonParserSpy = spy(new AmazonHTMLParser)
  }

  test("read no book title from no html") {
    amazonParser.extractBookTitleFromDocument(documentFromNoHtml) should equal("")
  }

  test("read no book title from html") {
    amazonParser.extractBookTitleFromDocument(documentNoBookNoReview) should equal("")
  }

  test("read valid book title from html") {
    amazonParser.extractBookTitleFromDocument(documentBookNoReview) should equal("Book 1")
  }

  test("read no book review count from no html") {
    amazonParser.extractReviewCountFromDocument(documentFromNoHtml) should equal(0)
  }

  test("read no book review count from html") {
    amazonParser.extractReviewCountFromDocument(documentNoBookNoReview) should equal(0)
  }

  test("read book review count from html") {
    amazonParser.extractReviewCountFromDocument(documentBookAndReview) should equal(1003)
  }

  test("test resultForISBN for isbn where title and review count are absent") {
    doReturn(documentNoBookNoReview).when(amazonParserSpy)
      .convertURLToDocument("http://www.amazon.com/exec/obidos/ASIN/0000000001")

    amazonParserSpy.resultForISBN("0000000001") should equal(("", 0))
  }

  test("test resultForISBN for isbn where book title is present and review count is absent") {
    doReturn(documentBookNoReview).when(amazonParserSpy)
      .convertURLToDocument("http://www.amazon.com/exec/obidos/ASIN/0000000001")

    amazonParserSpy.resultForISBN("0000000001") should equal(("Book 1", 0))
  }

  test("test resultForISBN for isbn, where both title and review count are presnet") {
    doReturn(documentBookAndReview).when(amazonParserSpy)
      .convertURLToDocument("http://www.amazon.com/exec/obidos/ASIN/0000000001")

    amazonParserSpy.resultForISBN("0000000001") should equal(("Book 1", 1003))
  }

  test("exception test for resultForISBN") {
    try {
      amazonParser.resultForISBN("0000000001")
      fail("Exception expected..")
    } catch {
      case e: RuntimeException =>
    }
  }

  test("exception test for convertURLToDocument") {
    try {
      amazonParser.convertURLToDocument("http://www.amazon.com/exec/obidos/ASIN/0000000001")
      fail("Exception expected..")
    } catch {
      case e: RuntimeException =>
    }
  }

  test("integration test for convertURLToDocument which return document class") {
    try {
      amazonParser.convertURLToDocument("http://www.amazon.com/exec/obidos/ASIN/1680500546").getClass should equal(classOf[Document])
    } catch {
      case e: RuntimeException => e.getMessage should equal("503: Service Unavailable")
      case default: Throwable => fail("Expected 503 exception or Document returned")
    }
  }

  test("integration test for resultForISBN, valid isbn as input") {
    try {
      val result = amazonParser.resultForISBN("1680500546")

      assert((result._1.equals("Pragmatic Scala: Create Expressive, Concise, and Scalable Applications") && result._2 >= 0))
    } catch {
      case e: RuntimeException => e.getMessage should equal("503: Service Unavailable")
      case default: Throwable => fail("Expected 503 exception or expected result")
    }
  }
}


