package game

import scala.util.Random

trait Item {
  def name: String
  def cd: Int
  def _type: String
}

case class Weapon(
  name: String,
  twoHanded: Boolean,
  cd: Int,
  baseDamage: Int,
  _type: String = "weapon"
) extends Item {
  def getDamage: Int = {
    val damageMultiplier: Double = Random.between(85, 115).toDouble / 100
    val finalDamage = (baseDamage * damageMultiplier).round.toInt
    // all crit chances and crit values goes to game.Opponent

    finalDamage
  }

}

case class Armor(
  name: String,
  cd: Int,
  armor: Int,
  _type: String = "armor"
) extends Item {

}
