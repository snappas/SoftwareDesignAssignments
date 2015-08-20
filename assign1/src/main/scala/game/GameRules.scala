package game

import Constants._

object GameRules {

  def isAlive(status: Boolean, numberOfLiveNeighbors: Int) =
    numberOfLiveNeighbors == 3 || status == ALIVE && numberOfLiveNeighbors == 2

  def countLiveNeighbors(grid: Array[Array[Boolean]], xCoord: Int, yCoord: Int) = {
    var count = 0

    for (y <- -1 to 1) {
      for (x <- -1 to 1) {
        if (checkOutOfBounds(grid, xCoord + x, yCoord + y) && grid(yCoord + y)(xCoord + x) == ALIVE)
          count += 1
      }
    }

    if (grid(yCoord)(xCoord)) count - 1 else count
  }

  private def checkOutOfBounds(grid: Array[Array[Boolean]], xCoord: Int, yCoord: Int) =
    xCoord >= 0 && yCoord >= 0 && xCoord < grid(0).length && yCoord < grid.length

  def nextGeneration(currentGrid: Array[Array[Boolean]]) = {
    val nextGrid = currentGrid.map(_.clone)

    for (y <- currentGrid.indices) {
      for (x <- currentGrid(y).indices) {
        nextGrid(y)(x) = isAlive(currentGrid(y)(x), countLiveNeighbors(currentGrid, x, y))
      }
    }

    nextGrid
  }

}
