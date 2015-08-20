package game

import game.avatars.{ Bike, Car }
import game.avatars.Avatar

object AvatarFactory {

  def getAvatarList(avatars: String*) = {
    avatars.toList.map(createAvatarFromName).filter(_ != null)
  }

  protected[game] def createAvatarFromName(avatarName: String) = {
    try {
      Class.forName("game.avatars." + avatarName).newInstance().asInstanceOf[Avatar]
    } catch {
      case e: Throwable => null
    }
  }

}
