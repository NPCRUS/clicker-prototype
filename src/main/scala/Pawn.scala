object Pawn {
  def apply(items: List[Item]): Pawn = new Pawn(items)
}

class Pawn(items: List[Item]) {
  def items(): List[Item] = items
}
