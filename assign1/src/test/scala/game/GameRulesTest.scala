package game

import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.TableDrivenPropertyChecks
import Constants._

@RunWith(classOf[JUnitRunner])
class GameRulesTest extends FunSuite with Matchers with TableDrivenPropertyChecks {

  val testLiveCells = Table(("numberOfLiveNeighbors", "outputState"),
    (0, DEAD), (1, DEAD), (2, ALIVE),
    (3, ALIVE), (4, DEAD), (8, DEAD))

  for ((numberOfLiveNeighbors, outputState) <- testLiveCells) {
    test("a live cell with " + numberOfLiveNeighbors + " live neighbors") {
      GameRules.isAlive(ALIVE, numberOfLiveNeighbors) should equal(outputState)
    }
  }

  val testDeadCells = Table(("numberOfLiveNeighbors", "outputState"),
    (0, DEAD), (1, DEAD), (2, DEAD),
    (3, ALIVE), (4, DEAD), (8, DEAD))

  for ((numberOfLiveNeighbors, outputState) <- testDeadCells) {
    test("a dead cell with " + numberOfLiveNeighbors + " live neighbors") {
      GameRules.isAlive(DEAD, numberOfLiveNeighbors) should equal(outputState)
    }
  }

  val testCountLiveNeighbors1 = Table(("xCoord", "yCoord"),
    (0, 0), (1, 1), (2, 2), (3, 3), (4, 4))

  for ((xCoord, yCoord) <- testCountLiveNeighbors1) {
    test("test live neighbors of position (" + xCoord + "," + yCoord + ") for 5*5 grid, empty initial state") {
      GameRules.countLiveNeighbors(Array.ofDim[Boolean](5, 5), xCoord, yCoord) should equal(0)
    }
  }
  val intialState = Array(Array(false, false, false, false, false),
    Array(false, false, true, false, false),
    Array(false, false, true, false, false),
    Array(false, false, true, false, false),
    Array(false, false, false, false, false))

  val testCountLiveNeighbors2 = Table(("xCoord", "yCoord", "output"),
    (0, 0, 0), (1, 1, 2), (2, 2, 2), (3, 3, 2), (4, 4, 0))

  for ((xCoord, yCoord, output) <- testCountLiveNeighbors2) {
    test("test live neighbors of position (" + xCoord + "," + yCoord + ") for 5*5 grid, intial state = " + intialState) {
      GameRules.countLiveNeighbors(intialState, xCoord, yCoord) should equal(output)
    }
  }

  test("test empty grid next generation") {
    val testGrid = Array(
      Array(false, false, false, false),
      Array(false, false, false, false))
    GameRules.nextGeneration(testGrid) should be(testGrid)
  }

  val verticalOscillator = Array(
    Array(false, false, false, false, false),
    Array(false, false, true, false, false),
    Array(false, false, true, false, false),
    Array(false, false, true, false, false),
    Array(false, false, false, false, false))

  val horizontalOscillator = Array(Array(false, false, false, false, false),
    Array(false, false, false, false, false),
    Array(false, true, true, true, false),
    Array(false, false, false, false, false),
    Array(false, false, false, false, false))

  test("test horizontal oscillator to vertical oscillator") {
    GameRules.nextGeneration(horizontalOscillator) should be(verticalOscillator)
  }

  test("test vertical oscillator to horizontal oscillator") {
    GameRules.nextGeneration(verticalOscillator) should be(horizontalOscillator)
  }

}
