object Pawn {
  def apply(name: String, items: List[Item]): Pawn = new Pawn(name, items)
}

class Pawn(val name: String, val items: List[Item]) {

}
