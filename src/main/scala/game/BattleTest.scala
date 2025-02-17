package game

import game.items._

object BattleTest extends App {
  val knife = Dagger(
    "knife",
    cd = 10000,
    20,
    DamageType.Physical,
    Rarity.Mediocre,
    List(PassiveEffect(EffectTargetType.PhysicMastery, 1000)),
    List(PeriodicActiveEffect(EffectTargetType.Hp, chance = 1.0, change = 10, self = true, 4, 2000)))
  val sword = Sword(
    "sword",
    7500,
    15,
    twoHanded = false
  )
  val handle1 = DualHandle(knife, sword)
  val pawn1 = Pawn("Mate", handle1, ArmorSet.empty, InitialProperties())

  val pitchfork = Polearm(
    "pitchfork",
    cd = 7000,
    15,
    twoHanded = true,
    DamageType.Physical,
    Rarity.Mediocre,
    List(PassiveEffect(EffectTargetType.PhysicMastery, 1000))
  )
  val handle2 = TwoHandedHandle(pitchfork)
  val body = Body("best", 10000, 100, ArmorType.Heavy, Rarity.Mediocre, List.empty)
  val armorSet = ArmorSet.empty.copy(body = Some(body))
  val pawn2 = Pawn("John", handle2, armorSet, InitialProperties(100))

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()

  actions.map(a => println(a.toString))
}
