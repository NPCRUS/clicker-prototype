package game

object Opponent {
  def apply(pawn: Pawn): Opponent = new Opponent(pawn, pawn.hp)
}

class Opponent(val pawn: Pawn, val hp: Int) {
  val armorSum: Int = pawn.armorSet.getAll.map(_.armor).sum

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
