package game

object BattleTest extends App {
  val knife = Dagger("knife", 920, 2)
  val handle1 = OneHandedHandle(knife, None)
  val body = Body("fucking great armor", 5000, 3)
  val armorSet = new ArmorSet(None, Some(body), None, None, None, None, None, None)
  val pawn1 = Pawn("Mate", 40, handle1, armorSet)

  val pitchfork = Polearm("pitchfork", 2300, 5, twoHanded = true)
  val handle2 = TwoHandedHandle(pitchfork)
  val pawn2 = Pawn("John", 40, handle2, ArmorSet.empty)

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()

  actions.map(a => println(a.toString))
}
