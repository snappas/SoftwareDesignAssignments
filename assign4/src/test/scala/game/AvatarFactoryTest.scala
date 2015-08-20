package game

import game.avatars.{ Bike, Car }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, FunSuite }
import org.scalatest.BeforeAndAfter

@RunWith(classOf[JUnitRunner])
class AvatarFactoryTest extends FunSuite with Matchers with BeforeAndAfter {

  test("get a class of Bike from the string Bike") {
    AvatarFactory.createAvatarFromName("Bike").getClass should be(classOf[Bike])
  }

  test("get a nonexistent class from string") {
    AvatarFactory.createAvatarFromName("DoesNotExist") should be(null)
  }

  test("get a class of Car from the string Car") {
    AvatarFactory.createAvatarFromName("Car").getClass should be(classOf[Car])
  }

  test("getAvatarList containing one Bike class when Bike is passed as string") {
    val bikeObjects = AvatarFactory.getAvatarList("Bike")
    bikeObjects.head.getClass should be(classOf[Bike])
  }

  test("getAvatarList of avatar fatory with Bike and nonexistant avatar's should have a length of 1") {
    val objectList = AvatarFactory.getAvatarList("Bike", "nonexistant")
    objectList.length should be(1)
  }

  test("getAvatarList containing Car and Bike classes from array of strings") {
    val objectList = AvatarFactory.getAvatarList("Car", "Bike")
    (objectList(0).getClass, objectList(1).getClass) should be((classOf[Car], classOf[Bike]))
  }

}


