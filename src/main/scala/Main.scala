object Main extends App {
  val knife = new Weapon("knife", false, 920, 2)
  val pitchfork = new Weapon("pitchfork", true, 2300,5)

  val pawn1 = Pawn("Mate", 40, List(knife), ArmorSet.empty)
  val pawn2 = Pawn("John", 40, List(pitchfork), ArmorSet.empty)

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()

  actions.map(a => println(a.toString))
}
