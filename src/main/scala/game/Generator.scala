package game

import game.items._
import game.items.Weapon

import scala.util.Random

object Generator {
  def generateBotEnemy(mapLevel: Int): Pawn = {
    val hp = 100 + mapLevel * 15

    val initialProperties = InitialProperties(hp)

    val weapon = randomWeapon(mapLevel)
    val handle = OneHandedHandle(weapon, None)
    val armorSet = ArmorSet(None, None, None, None)

    Pawn(generateBotName, handle, armorSet, initialProperties)

  }

  private def randomWeapon(mapLevel: Int): Weapon = {
    val listOfAdjective = List("hilarious", "demonic", "ubiquitous", "unreal", "masterful", "competitive", "communist", "medieval", "blunt", "rusty", "sharp", "heavy", "powerful", "black", "pink", "light", "balanced", "swiss", "huge", "great")

    val adjective = listOfAdjective.random
    val damage = 20 + mapLevel * 8
    val cd = 8000 - mapLevel * 100

    List(
      Sword(s"$adjective sword", cd, damage, twoHanded = false),
      Dagger(s"$adjective dagger", cd, damage),
      Polearm(s"$adjective polearm", cd, damage, twoHanded = false)
    ).random
  }

  private def generateBotName: String = {
    val firstPart = List("ugly", "pickled", "funny", "crooked", "blind", "deaf", "smart", "strong", "giant", "small", "hilarious", "soaked")
    val secondPart = List("bandit", "thug", "mercenary", "alchemist", "warrior", "viking")
    s"${firstPart.random} ${secondPart.random}"
  }

  implicit class RandomListExtension[T](list: List[T]) {
    def random: T = list(Random.nextInt(list.length))
  }
}
