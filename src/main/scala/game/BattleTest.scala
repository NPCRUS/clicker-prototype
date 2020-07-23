package game

object BattleTest extends App {
  val knife = Dagger(
    "knife",
    cd = 5000,
    15,
    DamageType.Physical,
    List(PassiveEffect(EffectTargetType.ColdRes, 1000)),
    List(LastingActiveEffect(ActiveEffectType.Lasting, EffectTargetType.Armor, chance = 1.0, change = -100, self = false, 6000)))
  val handle1 = OneHandedHandle(knife, None)
  val pawn1 = Pawn("Mate", handle1, ArmorSet.empty, InitialProperties())

  val pitchfork = Polearm(
    "pitchfork",
    cd = 5000,
    15,
    twoHanded = true,
    DamageType.Cold, List(PassiveEffect(EffectTargetType.ColdMit, 1000))
  )
  val handle2 = TwoHandedHandle(pitchfork)
  val body = Body("best", 10000, 100, List.empty)
  val armorSet = ArmorSet(None, Some(body), None, None, None, None, None, None)
  val pawn2 = Pawn("John", handle2, armorSet, InitialProperties(100))

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()

  actions.map(a => println(a.toString))
}
