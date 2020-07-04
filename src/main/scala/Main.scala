object Main extends App {
  val pawn1 = Pawn(List(Item(1600)))
  val pawn2 = Pawn(List(Item(2000)))

  val battle = new Battle(pawn1, pawn2)
  val actions = battle.calculate()
  println(
    actions.map(i => s"${i.timestamp.toDouble / 1000}s - ${i.item.cd()}")
  )
}
