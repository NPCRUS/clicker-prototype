package game

object BattleTest extends App {
  val knife = Dagger(
    "knife",
    cd = 10000,
    20,
    DamageType.Physical,
    List(PassiveEffect(EffectTargetType.ColdRes, 1000)),
    List(PeriodicActiveEffect(ActiveEffectType.Periodic, EffectTargetType.Armor, chance = 1.0, change = 50, self = true, 4, 2000)))
  val handle1 = OneHandedHandle(knife, None)
  val pawn1 = Pawn("Mate", handle1, ArmorSet.empty, InitialProperties())

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
  val pawn2 = Pawn("John", handle2, armorSet, InitialProperties(100))

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()

  actions.map(a => println(a.toString))
}
