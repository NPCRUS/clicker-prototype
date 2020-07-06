object Opponent {
  def apply(pawn: Pawn): Opponent = new Opponent(pawn, pawn.hp)
}

class Opponent(val pawn: Pawn, val hp: Int) {
  def calculateDamage(item: Weapon, baseDamage: Int): Int = {
    // logic for crits and any other sort of multipliers
    baseDamage
  }

  def dealDamage(damage: Int): (Int, Opponent) = {
    // place to some armor reduction and stuff
    (damage, new Opponent(pawn, hp - damage))
  }
}
