trait Action {
  def init: Opponent
  def target: Opponent
  def item: Item
  def timestamp: Int
}

object Attack {
  def apply(init: Opponent, target: Opponent, item: Weapon, timestamp: Int, damage: Int): Attack = {
    val initialDamage = init.calculateDamage(item)
    val (actualDamage, newTarget) = target.dealDamage(initialDamage)
    new Attack(init, newTarget, item, timestamp, actualDamage)
  }
}

class Attack(val init: Opponent, val target: Opponent, val item: Item, val timestamp: Int, val damage: Int) extends Action {
  override def toString: String =
    s"${init.pawn.name}(${init.hp}hp) attacked ${target.pawn.name}(${target.hp}) with ${item.name} dealing ${damage} damage, ${timestamp.toDouble / 1000}s"
}


