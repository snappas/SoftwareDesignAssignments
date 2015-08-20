package game

import game.avatars.{Avatar, Bike}
import org.scalatest.Matchers
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class PlayerTest extends FunSuite with Matchers with BeforeAndAfter with MockitoSugar {

  var avatarsAvailable: List[Avatar] = _
  var bike1: Avatar = _
  var bike2: Avatar = _
  var bike3: Avatar = _


  before {
    bike1 = new Bike
    bike2 = new Bike
    bike3 = new Bike
    avatarsAvailable = List(bike1, bike2, bike3)
  }

  test("bike's capability is called from player") {
    val spyBike = spy(new Bike)
    val player = new Player(spyBike, List())
    player.performAction()
    
    verify(spyBike).performAction()
  }

  test("next form of bike1 should be bike2, in a list of 3 bikes") {
    val player = new Player(bike1, avatarsAvailable)
    player.transformToNextForm()

    player.getCurrentAvatar() should be(bike2)
  }

  test("next form from bike3 should be bike1, in circular list") {
    val player = new Player(bike3, avatarsAvailable)
    player.transformToNextForm()

    player.getCurrentAvatar() should be(bike1)
  }

  test("previous form of bike2 should be bike1, in a list of 3 bikes") {
    val player = new Player(bike2, avatarsAvailable)
    player.transformToPreviousForm()

    player.getCurrentAvatar() should be(bike1)
  }

  test("previous from from bike1 should be bike3, in circular list") {
    val player = new Player(bike1, avatarsAvailable)
    player.transformToPreviousForm()

    player.getCurrentAvatar() should be(bike3)
  }


}
