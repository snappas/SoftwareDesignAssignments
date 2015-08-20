import game.{ Player, AvatarFactory }

object Driver {

  def main(args: Array[String]): Unit = {
    val avatarList = AvatarFactory.getAvatarList("Bike", "Car", "Plane", "Rocket", "NonExistant");
    val player = new Player(avatarList.head, avatarList)
    println("List of Avatars: " + avatarList)
    println("Current Avatar: " + player.getCurrentAvatar())
    
    for(i <- avatarList.indices){
      println("\n-----------Transform next----------")
      player.transformToNextForm()
      println("Current Avatar: " + player.getCurrentAvatar())
      println("Calling player.performAction() for " + player.getCurrentAvatar())
      player.performAction()
    }

    for(i <- avatarList.indices){
      println("\n-----------Transform Previous----------")
      player.transformToPreviousForm()
      println("Current Avatar: " + player.getCurrentAvatar())
      println("Calling player.performAction() for " + player.getCurrentAvatar())
      player.performAction()
    }
  }

}
