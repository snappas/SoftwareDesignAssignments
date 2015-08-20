package fibonacci

import scala.collection.mutable.Map

object FibonacciNumber {

  def iterativeFibonacci(position: Int) =
    (2 to position).foldLeft((1, 1)) { (prev, ignore) => (prev._2, prev._1 + prev._2) }._2

  def recursiveFibonacci(position: Int): Int = {
    position match {
      case 0 | 1 => 1
      case _ => recursiveFibonacci(position - 1) + recursiveFibonacci(position - 2)
    }
  }

  var cache = Map(0 -> 1, 1 -> 1)

  def memoizedFibonacci(position: Int): Int = {
    cache.getOrElseUpdate(position, memoizedFibonacci(position - 1) + memoizedFibonacci(position - 2))
  }

}
