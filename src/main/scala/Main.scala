object Main extends App {
  val knife = new Weapon(1600, 2, "knife")
  val pitchfork = new Weapon(2300, 5, "pitchfork")

  val pawn1 = Pawn("Mate", List(knife))
  val pawn2 = Pawn("John", List(pitchfork))

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()

  actions.map(a => println(a.toString))
}
