package game

object Pawn {
  def apply(name: String, hp: Int, items: List[Item], armorSet: ArmorSet): Pawn =
    new Pawn(name, hp, items, armorSet)
}

case class Pawn(
  name: String,
  hp: Int,
  items: List[Item],
  armorSet: ArmorSet
) {

}
