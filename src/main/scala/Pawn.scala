object Pawn {
  def apply(name: String, hp: Int, items: List[Item]): Pawn = new Pawn(name, hp, items)
}

class Pawn(val name: String, val hp: Int, val items: List[Item]) {

}
