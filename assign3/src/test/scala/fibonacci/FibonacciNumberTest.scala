package fibonacci

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.TableDrivenPropertyChecks

@RunWith(classOf[JUnitRunner])
class FibonacciNumberTest extends FibonacciBehaviors with Matchers {

  "canany test" should " be true" in { true should be(true) }

  "Iterative fibonacci" should behave like validFibonacci(FibonacciNumber.iterativeFibonacci)

  "Recursive fibonacci" should behave like validFibonacci(FibonacciNumber.recursiveFibonacci)

  "Memoize fibonacci" should behave like validFibonacci(FibonacciNumber.memoizedFibonacci)

  "Memoized fibonacci " should " perform better than recursive fibonacci for position: 40" in {
    val fiboMemo = timeTaken(FibonacciNumber.memoizedFibonacci(40))
    val fiboRecursive = timeTaken(FibonacciNumber.recursiveFibonacci(40))

    fiboRecursive._2 should be > 10 * fiboMemo._2
  }

  def timeTaken[T](block: => T) = {
    val timeBefore = System.currentTimeMillis
    val returnValueOfBlock = block
    val timeAfter = System.currentTimeMillis
    (returnValueOfBlock, timeAfter - timeBefore)
  }

}

trait FibonacciBehaviors extends FlatSpec with TableDrivenPropertyChecks {

  val fibonacciTests = Table(("position", "fibonacciNumber"),
    (0, 1), (1, 1), (2, 2), (3, 3),
    (4, 5), (5, 8), (10, 89))

  def validFibonacci(fibFunction: Int => Int) {
    for ((position, expectedResult) <- fibonacciTests)
      it should "be " + expectedResult + " for " + position in {
        assert(fibFunction(position) == expectedResult)
      }
  }

}
