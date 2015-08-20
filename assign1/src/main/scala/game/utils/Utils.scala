package game.utils

import game.Constants._

import scala.collection.mutable
import scala.collection.mutable.Map
import scalafx.scene.layout.StackPane

object Utils {

  def getInitialGrid(rows: Int, columns: Int, initialCells: Array[(Int, Int)]): Array[Array[Boolean]] = {
    val grid = Array.ofDim[Boolean](rows, columns)
    for ((x, y) <- initialCells) {
      grid(x)(y) = true
    }
    grid
  }

}
