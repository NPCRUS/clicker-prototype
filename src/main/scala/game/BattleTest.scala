package game

object BattleTest extends App {
  val knife = Weapon("knife", 920, 2, twoHanded = false)
  val pitchfork = Weapon("pitchfork", 2300, 5, twoHanded = true)

  val body = Body("fucking great armor", 1, 3)
  val armorSet = new ArmorSet(None, Some(body), None, None, None, None, None, None)

  val pawn1 = Pawn("Mate", 40, List(knife), armorSet)
  val pawn2 = Pawn("John", 40, List(pitchfork), ArmorSet.empty)

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()

  actions.map(a => println(a.toString))
}
