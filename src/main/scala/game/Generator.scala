package game

import game.items._

object Generator {
  def generateBotEnemy(character: Pawn, mapLevel: Int): Pawn = {
    val pitchfork = Polearm(
      "pitchfork",
      cd = 7000,
      15,
      twoHanded = true,
      DamageType.Physical, List(PassiveEffect(EffectTargetType.ColdMit, 1000))
    )
    val handle2 = TwoHandedHandle(pitchfork)
    val body = Body("best", 10000, 100, List.empty)
    val armorSet = ArmorSet(None, Some(body), None, None, None, None, None, None)

    Pawn("John", handle2, armorSet, InitialProperties(100))
  }
}
