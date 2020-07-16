package game

object Pawn {
  def apply(name: String, hp: Int, handle: Handle, armorSet: ArmorSet): Pawn =
    new Pawn(name, hp, handle, armorSet)
}

case class Pawn(
  name: String,
  hp: Int,
  handle: Handle,
  armorSet: ArmorSet
) {
  def getAllActiveItems: List[Item] = handle.getWeapons ++ armorSet.getAll
}
