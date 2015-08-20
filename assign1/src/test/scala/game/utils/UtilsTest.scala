package game.utils

import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.TableDrivenPropertyChecks

@RunWith(classOf[JUnitRunner])
class UtilsTest extends FunSuite with Matchers with TableDrivenPropertyChecks {

  val tests = Table(("rows", "columns", "intialCells", "output"),
    (1, 1, Array((0, 0)), Array(Array(true))),
    (2, 3, Array((1, 0), (1, 1), (1, 2)), Array(Array(false, false, false), Array(true, true, true))),
    ((5, 5, Array((2, 1), (2, 2), (2, 3)), Array(Array(false, false, false, false, false),
      Array(false, false, false, false, false),
      Array(false, true, true, true, false),
      Array(false, false, false, false, false),
      Array(false, false, false, false, false)))))

  for ((rows, columns, intialCells, output) <- tests) {
    Utils.getInitialGrid(rows, columns, intialCells) should equal(output)
  }

  test("test index out of bound exception on getInitialGrid") {
    try {
      Utils.getInitialGrid(1, 1, Array((2, 2)))
      fail()
    } catch {
      case _: IndexOutOfBoundsException => //Expected, so continue
    }
  }

}
