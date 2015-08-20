package reviewscollector

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
class ReviewsCollectorTest extends FunSuite with Matchers with BeforeAndAfter with TableDrivenPropertyChecks with MockitoSugar with Inside {

  var reviewsCollector: ReviewsCollector = _
  var isbnSerivceMock: ISBNService = _

  before {
    isbnSerivceMock = mock[ISBNService]
    reviewsCollector = new ReviewsCollector
    reviewsCollector.setISBNService(isbnSerivceMock)
  }

  test("canary test") {
    true should be(true)
  }

  val testSortBasedOnTitles = Table(("input", "output"),
    (List(), List()),
    (List(("0000000001", "aa", 1)), List(("0000000001", "aa", 1))),
    (List(("0000000001", "ba", 1), ("0000000002", "aa", 1), ("0000000003", "ab", 1)), List(("0000000002", "aa", 1), ("0000000003", "ab", 1), ("0000000001", "ba", 1))),
    (List(("0000000001", "ba", 1), ("0000000002", "aa", 1), ("0000000003", "ab", 1), ("0000000004", "ab", 0)), List(("0000000002", "aa", 1), ("0000000004", "ab", 0), ("0000000003", "ab", 1), ("0000000001", "ba", 1))))

  for ((input, output) <- testSortBasedOnTitles) {
    test("test getSortedListOfTuples for input list: " + input) {
      reviewsCollector.getSortedListOfTuples(input) should equal(output)
    }
  }

  val testCalculateTotalNumberOfReviews = Table(("input", "output"),
    (List(), 0),
    (List(("0000000001", "aa", 1)), 1),
    (List(("0000000001", "ba", 2), ("0000000002", "aa", 3), ("0000000003", "ab", 5)), 10))

  for ((input, output) <- testCalculateTotalNumberOfReviews) {
    test("test calculateTotalNumberOfReviews for list: " + input) {
      reviewsCollector.calculateTotalNumberOfReviews(input) should equal(output)
    }
  }

  test("test getResultForISBN for correct isbn") {
    when(isbnSerivceMock.resultForISBN("0000000001")).thenReturn(("Book 1", 3))

    reviewsCollector.getResultForISBN("0000000001") should equal("0000000001", "Book 1", 3, "")
  }

  test("test getResultForISBN for isbn which throws error") {
    when(isbnSerivceMock.resultForISBN("0000000002")).thenThrow(new RuntimeException("ISBN is wrong"))

    reviewsCollector.getResultForISBN("0000000002") should equal("0000000002", "", 0, "ISBN is wrong")
  }

  test("test getResultForListOfISBN for empty isbn list") {
    reviewsCollector.getResultForListOfISBN(List()) should equal(List(), 0, List())
  }

  test("test getResultForListOfISBN containing one isbn number, service returns correctly") {
    when(isbnSerivceMock.resultForISBN("0000000001")).thenReturn(("Book 1", 3))

    reviewsCollector.getResultForListOfISBN(List("0000000001")) should equal(List(("0000000001", "Book 1", 3)), 3, List());
  }

  test("test getResultForListOfISBN for multiple isbn numbers, none of the service hit throws exception") {
    when(isbnSerivceMock.resultForISBN("0000000001")).thenReturn(("Book 6", 3))
    when(isbnSerivceMock.resultForISBN("0000000002")).thenReturn(("Book 3", 2))
    when(isbnSerivceMock.resultForISBN("0000000003")).thenReturn(("Book 3", 4))

    reviewsCollector.getResultForListOfISBN(List("0000000001", "0000000002", "0000000003")) should equal(List(("0000000002", "Book 3", 2), ("0000000003", "Book 3", 4), ("0000000001", "Book 6", 3)), 9, List())
  }

  test("test getResultForListOfISBN for mutliple isbn numbers, where a few of the service hits throw exception") {
    when(isbnSerivceMock.resultForISBN("0000000001")).thenReturn(("Book 42", 92))
    when(isbnSerivceMock.resultForISBN("0000000002")).thenReturn(("Book 10", 9872))
    when(isbnSerivceMock.resultForISBN("0000000003")).thenReturn(("Book 98", 712))
    when(isbnSerivceMock.resultForISBN("0000000004")).thenThrow(new RuntimeException("Unable to fetch data"))

    val actualListOfResultsAndErrosTuple = reviewsCollector.getResultForListOfISBN(List("0000000001", "0000000002", "0000000003", "0000000004"))
    val expecteListOfResults = List(("0000000002", "Book 10", 9872), ("0000000001", "Book 42", 92), ("0000000003", "Book 98", 712))
    val expectedTotalReviewCount = 10676
    val expectedListOfErros = List(("0000000004", "Unable to fetch data"))
    actualListOfResultsAndErrosTuple should equal(expecteListOfResults, expectedTotalReviewCount, expectedListOfErros)
  }

  test("integration test for invalid isbn with ReviewsCollector") {
    reviewsCollector.setISBNService(new AmazonHTMLParser)

    reviewsCollector.getResultForISBN("0000000001") should equal("0000000001", "", 0, "404: NotFound")
  }

  test("integration test for valid isbn with ReviewsCollector") {
    reviewsCollector.setISBNService(new AmazonHTMLParser)

    inside(reviewsCollector.getResultForISBN("1680500546")) {
      case ("1680500546", "Pragmatic Scala: Create Expressive, Concise, and Scalable Applications", reviewCount, "") => reviewCount should be >= 0
      case ("1680500546", "", 0, "503: Service Unavailable") =>
      case _ => fail("Expected 503 exception or expected result")
    }
  }

  test("integration test for list of isbn from file") {
    reviewsCollector.setISBNService(new AmazonHTMLParser)
    val isbnListFromFile = Source.fromInputStream(getClass.getResourceAsStream("/1isbn10.txt")).getLines()

    inside(reviewsCollector.getResultForListOfISBN(isbnListFromFile.toList)) {
      case (List(("1680500546", "Pragmatic Scala: Create Expressive, Concise, and Scalable Applications", reviewCount)), totalReviewCount, List()) =>
        {
          reviewCount should be >= 0
          totalReviewCount should be >= 0
        }
      case (List(), 0, List(("1680500546", "503: Service Unavailable"))) =>
      case _ => fail("Expected 503 exception or expected result")
    }
  }

}

