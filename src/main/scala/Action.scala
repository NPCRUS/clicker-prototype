trait Action {
  def pawn: Pawn
  def item: Item
  def timestamp: Int
}

object Attack {
  def apply(pawn: Pawn, item: Item, timestamp: Int, damage: Int): Attack =
    new Attack(pawn, item, timestamp, damage)
}
class Attack(val pawn: Pawn, val item: Item, val timestamp: Int, val damage: Int) extends Action {
  override def toString: String =
    s"${pawn.name} attacked with ${item.name} dealing ${damage} damage, ${timestamp.toDouble / 1000}s"
}


