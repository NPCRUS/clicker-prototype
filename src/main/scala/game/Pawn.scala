package game

object Pawn {
  def apply(name: String, hp: Int, items: List[Item], armorSet: ArmorSet): Pawn =
    new Pawn(name, hp, items, armorSet)
}

class Pawn(val name: String, val hp: Int, val items: List[Item], val armorSet: ArmorSet) {

}
