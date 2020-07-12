package game

object Opponent {
  def apply(pawn: Pawn, hp: Int): Opponent = new Opponent(pawn, hp)
  def fromPawn(pawn: Pawn): Opponent = new Opponent(pawn, pawn.hp)
}

case class Opponent(
  pawn: Pawn,
  hp: Int
) {
  lazy val armorSum: Int = pawn.armorSet.getAll.map(_.armor).sum

  def calculateDamage(item: Weapon, baseDamage: Int): Int = {
    // logic for crits and any other sort of multipliers
    baseDamage
  }

  def dealDamage(damage: Int): (Int, Opponent) = {
    val armorMitigation = armorSum.toDouble / (armorSum + 10 * damage)

    val actualDamage = (damage * (1 - armorMitigation)).round.toInt
    (actualDamage, new Opponent(pawn, hp - actualDamage))
  }
}
