object Opponent {
  def apply(pawn: Pawn): Opponent = new Opponent(pawn, pawn.hp)
}

class Opponent(val pawn: Pawn, val hp: Int) {
  def dealDamage(damage: Int): Opponent = new Opponent(pawn, hp - damage)
}
