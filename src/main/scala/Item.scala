import scala.util.Random

trait Item {
  def cd: Int
  def name: String
}

class Weapon(val cd: Int,val baseDamage: Int, val name: String) extends Item {
  def getDamage: Int = {
    val damageMultiplier: Double = Random.between(85, 115).toDouble / 100
    val finalDamage = (baseDamage * damageMultiplier).round.toInt
    // all crit chances and chrit values goes to Opponent

    finalDamage
  }
}
