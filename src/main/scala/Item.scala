import scala.util.Random

trait Item {
  def cd: Int
  def name: String
}

class Weapon(
  val name: String,
  val twoHanded: Boolean,
  val cd: Int,
  val baseDamage: Int
) extends Item {
  def getDamage: Int = {
    val damageMultiplier: Double = Random.between(85, 115).toDouble / 100
    val finalDamage = (baseDamage * damageMultiplier).round.toInt
    // all crit chances and crit values goes to Opponent

    finalDamage
  }
}

class Armor(val name: String, val cd: Int) extends Item {

}
