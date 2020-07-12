package game

trait Action {
  def init: Opponent
  def target: Opponent
  def item: Item
  def timestamp: Int
  def _type: String
}

object Attack {
  def apply(init: Opponent, target: Opponent, item: Weapon, timestamp: Int): Attack = {
    val weaponBaseDamage = item.getDamage
    val initialInputDamage = init.calculateDamage(item, weaponBaseDamage)
    val (finalDamage, newTarget) = target.dealDamage(initialInputDamage)
    new Attack(init, newTarget, item, timestamp, finalDamage)
  }
}

case class Attack(
  init: Opponent,
  target: Opponent,
  item: Item,
  timestamp: Int,
  damage: Int,
  _type: String = "attack"
) extends Action {
  override def toString: String =
    s"${init.pawn.name}(${init.hp}hp) attacked ${target.pawn.name}(${target.hp}) with ${item.name} dealing ${damage} damage, ${timestamp.toDouble / 1000}s"
}


