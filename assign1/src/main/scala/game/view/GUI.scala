package game.view

import game.Constants._
import game.GameRules

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.control.Label
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.GridPane
import scalafx.geometry.Insets
import scalafx.scene.control.TextField
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.RowConstraints
import scalafx.geometry.HPos
import scalafx.geometry.VPos
import scalafx.scene.layout.HBox
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.animation.Timeline
import scalafx.animation.KeyFrame
import scalafx.util.Duration
import scalafx.event.ActionEvent

class GUI extends JFXApp {

  val inputPane = new GridPane
  val bottomControls = new HBox
  val gridContainer = new StackPane
  val gridPane = new GridPane
  val timeLine = new Timeline

  val rowValue = new TextField {
    text = "10"
  }
  val columnValue = new TextField {
    text = "10"
  }

  var rows = Integer.parseInt(rowValue.text.value)
  var columns = Integer.parseInt(columnValue.text.value)

  var cells = Array.ofDim[StackPane](rows, columns)
  var grid = Array.ofDim[Boolean](rows, columns)

  stage = new PrimaryStage {
    title = "Game of Life"
    height = 350
    width = 350
    scene = new Scene {
      root = new BorderPane {
        top = inputPane
        center = gridContainer
        bottom = bottomControls
      }
    }
  }

  setInputPane()
  setMainContent()
  initializeCells()
  setGridProperties()
  setBottomControls()
  setTimelineProperties()

  def setInputPane() {
    inputPane.hgap = 10
    inputPane.vgap = 10
    inputPane.padding = Insets(20, 100, 10, 10)
    inputPane.add(new Label("Rows:"), 0, 0)
    inputPane.add(rowValue, 1, 0)
    inputPane.add(new Label("Columns:"), 0, 1)
    inputPane.add(columnValue, 1, 1)
    val applyButton = new Button("Apply") {
      onAction = handle {
        if (isNumeric1to100(rowValue.text.value) && isNumeric1to100(columnValue.text.value)) {
          rows = Integer.parseInt(rowValue.text.value)
          columns = Integer.parseInt(columnValue.text.value)
          cells = Array.ofDim[StackPane](rows, columns)
          grid = Array.ofDim[Boolean](rows, columns)
          gridPane.children.clear()
          gridPane.columnConstraints.clear()
          gridPane.rowConstraints.clear()
          initializeCells()
          setGridProperties()
        }
      }
    }
    inputPane.add(applyButton, 0, 2)
  }

  def setMainContent() {
    gridContainer.style = "-fx-background-color: whitesmoke; -fx-padding: 10;"
    gridContainer.children += gridPane
  }

  def initializeCells() {
    for (row <- grid.indices)
      for (column <- grid(row).indices) {
        cells(row)(column) = new StackPane {
          style = DEAD_CELL
          onMouseClicked = handle {
            if (grid(row)(column) == ALIVE) {
              style = DEAD_CELL
              grid(row)(column) = DEAD
            } else {
              style = ALIVE_CELL
              grid(row)(column) = ALIVE
            }
          }
        }
        gridPane.add(cells(row)(column), column, row)
      }
  }

  def setGridProperties() {
    gridPane.style = "-fx-background-color: black; -fx-padding: 2; -fx-hgap: 2; -fx-vgap: 2;"
    gridPane.snapToPixel = false

    for (i <- 1 to columns) {
      val columnInfo = new ColumnConstraints {
        percentWidth = 100.0 / columns
        halignment = HPos.Center
      }
      gridPane.columnConstraints += columnInfo
    }
    for (i <- 1 to rows) {
      val rowInfo = new RowConstraints {
        percentHeight = 100.0 / rows
        valignment = VPos.Center
      }
      gridPane.rowConstraints += rowInfo
    }
  }

  def setBottomControls() {
    val exitButton = new Button("Exit") { onAction = handle { System.exit(0) } }
    val startButton = new Button("Start") {
      onAction = handle {
        inputPane.children.clear()
        timeLine.play()
        disable = true
      }
    }
    bottomControls.style = "-fx-background-color: #336699;"
    bottomControls.children.addAll(startButton, exitButton)
  }

  def setTimelineProperties() {
    timeLine.cycleCount = Timeline.Indefinite
    timeLine.keyFrames = KeyFrame(Duration(300), onFinished = handle {
      grid = GameRules.nextGeneration(grid)
      updateGrid()
    })
  }

  def updateGrid() {
    for (row <- grid.indices)
      for (column <- grid(row).indices)
        if (grid(row)(column) == ALIVE)
          cells(row)(column).style = ALIVE_CELL
        else
          cells(row)(column).style = DEAD_CELL
  }

  def isNumeric1to100(input: String) = input.matches("\\d{1,2}(?!\\d)|100")
}

