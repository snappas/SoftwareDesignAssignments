package game

import game.avatars.Avatar

class Player(private var currentAvatar: Avatar, private var availableAvatars: List[Avatar]) {

  def performAction() {
    currentAvatar.performAction()
  }

  def getCurrentAvatar() = {
    currentAvatar
  }

  def transformToNextForm() {
    val currentIndex = availableAvatars.indexOf(currentAvatar)
    val nextIndex = (currentIndex + 1) % availableAvatars.size
    currentAvatar = availableAvatars(nextIndex)
  }

  def transformToPreviousForm() {
    val currentIndex = availableAvatars.indexOf(currentAvatar)
    val prevIndex = Math.floorMod(currentIndex - 1, availableAvatars.size)
    currentAvatar = availableAvatars(prevIndex)
  }

}
