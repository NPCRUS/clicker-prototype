package game

import game.ArmorType.ArmorType
import game.WeaponType.WeaponType

import scala.util.Random

trait Item {
  def name: String
  def cd: Int
  def _type: String
}

object Weapon {
  def unapply(arg: Weapon): Option[Weapon] = arg match {
    case s: Sword => Some(s)
    case d: Dagger => Some(d)
    case p: Polearm => Some(p)
  }
}

trait Weapon extends Item {
  def weaponType: WeaponType.WeaponType
  def baseDamage: Int
  def twoHanded: Boolean

  override def _type: String = "weapon"

  def getDamage: Int = {
    val damageMultiplier: Double = Random.between(85, 115).toDouble / 100
    val finalDamage = (baseDamage * damageMultiplier).round.toInt
    // all crit chances and crit values goes to game.Opponent

    finalDamage
  }
}

object WeaponType extends Enumeration {
  type WeaponType = Value
  val Scepter, Mace, Axe, Sword, Bow, Dagger, Staff, Wand, Polearm = Value
}

case class Sword(
  name: String,
  cd: Int,
  baseDamage: Int,
  twoHanded: Boolean
) extends Weapon {
  override def weaponType: WeaponType = WeaponType.Sword
}

case class Dagger(
  name: String,
  cd: Int,
  baseDamage: Int
) extends Weapon {
  override def weaponType: WeaponType = WeaponType.Dagger
  override def twoHanded: Boolean = false
}

case class Polearm(
  name: String,
  cd: Int,
  baseDamage: Int,
  twoHanded: Boolean
) extends Weapon {
  override def weaponType: WeaponType = WeaponType.Polearm
}

trait Armor extends Item {
  def armorType: ArmorType.ArmorType
  def armor: Int

  override def _type: String = "armor"
}

object ArmorType extends Enumeration {
  type ArmorType = Value
  val Helmet, Body, Gloves, Boots, Belt, Amulet, Ring, Shield = Value
}

case class Helmet(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  override def armorType: ArmorType = ArmorType.Helmet
}

case class Body(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  override def armorType: ArmorType = ArmorType.Body
}

case class Gloves(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  override def armorType: ArmorType = ArmorType.Gloves
}

case class Boots(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  override def armorType: ArmorType = ArmorType.Boots
}

case class Belt(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  override def armorType: ArmorType = ArmorType.Belt
}

case class Amulet(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  override def armorType: ArmorType = ArmorType.Amulet
}

case class Ring(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  override def armorType: ArmorType = ArmorType.Ring
}

case class Shield(
  name: String,
  cd: Int,
  armor: Int
) extends Armor {
  override def armorType: ArmorType = ArmorType.Ring
}

