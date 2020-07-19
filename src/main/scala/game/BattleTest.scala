package game

object BattleTest extends App {
  val knife = Dagger(
    "knife",
    10000,
    15,
    DamageType.Physical,
    List(PassiveEffect(EffectTargetType.ColdRes, 1000)),
    List(OneTimeActiveEffect(ActiveEffectType.OneTime, EffectTargetType.Hp, chance = 0.3, change = 20, self = true)))
  val handle1 = OneHandedHandle(knife, None)
  val pawn1 = Pawn("Mate", handle1, ArmorSet.empty, InitialProperties())

  val pitchfork = Polearm("pitchfork", 9000, 15, twoHanded = true, DamageType.Cold, List(PassiveEffect(EffectTargetType.ColdMit, 1000)))
  val handle2 = TwoHandedHandle(pitchfork)
  // val body = Body("best", 1000, 100, List.empty)
  // val armorSet = ArmorSet(None, Some(body), None, None, None, None, None, None)
  val pawn2 = Pawn("John", handle2, ArmorSet.empty, InitialProperties(100))

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()

  actions.map(a => println(a.toString))
}
