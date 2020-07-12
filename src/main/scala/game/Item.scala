package game

import scala.util.Random

trait Item {
  def name: String
  def cd: Int
  def _type: String
}

case class Weapon(
  name: String,
  cd: Int,
  baseDamage: Int,
  twoHanded: Boolean,
) extends Item {
  override def _type: String = "weapon"
  def getDamage: Int = {
    val damageMultiplier: Double = Random.between(85, 115).toDouble / 100
    val finalDamage = (baseDamage * damageMultiplier).round.toInt
    // all crit chances and crit values goes to game.Opponent

    finalDamage
  }

}

trait Armor extends Item {
  def armorType: ArmorType.ArmorType
  def armor: Int

  override def _type: String = "armor"
}

object ArmorType extends Enumeration {
  type ArmorType = Value
  val Helmet, Body, Gloves, Boots, Belt, Amulet, Ring = Value
}

case class Helmet(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  val armorType: ArmorType.ArmorType = ArmorType.Helmet
}

case class Body(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  val armorType: ArmorType.ArmorType = ArmorType.Body
}

case class Gloves(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  val armorType: ArmorType.ArmorType = ArmorType.Gloves
}

case class Boots(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  val armorType: ArmorType.ArmorType = ArmorType.Boots
}

case class Belt(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  val armorType: ArmorType.ArmorType = ArmorType.Belt
}

case class Amulet(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  val armorType: ArmorType.ArmorType = ArmorType.Amulet
}

case class Ring(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  val armorType: ArmorType.ArmorType = ArmorType.Ring
}
