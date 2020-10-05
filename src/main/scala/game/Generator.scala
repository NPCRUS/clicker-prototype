package game

import game.items._

import scala.util.Random

object Generator {
  def generateBotEnemy(character: Pawn, mapLevel: Int): Pawn = {
    val hp = 100 + mapLevel * 15
    val dps = 20 + mapLevel * 8

    val pitchfork = Polearm(
      "pitchfork",
      cd = 7000,
      15,
      twoHanded = true,
      DamageType.Physical,
      Rarity.Mediocre,
      List(PassiveEffect(EffectTargetType.ColdMit, 1000))
    )
    val handle2 = TwoHandedHandle(pitchfork)
    val armorSet = ArmorSet(None, None, None, None, None, None, None, None)

    Pawn("John", handle2, armorSet, InitialProperties(100))

  }

  implicit class RandomListExtension[T](list: List[T]) {
    def random: T = list(Random.nextInt(list.length))
  }
}
