package game.items

import game.items.ItemType.Type

import scala.util.Random

trait Weapon extends Item {
  def weaponType: WeaponType.Type

  def baseDamage: Int

  def twoHanded: Boolean

  def damageType: DamageType.Type

  override def _type: Type = ItemType.Weapon

  def getDamage: Int = {
    val damageMultiplier: Double = Random.between(85, 115).toDouble / 100
    val finalDamage = (baseDamage * damageMultiplier).round.toInt
    // all crit chances and crit values goes to game.Opponent

    finalDamage
  }

  def isPhysical: Boolean = damageType == DamageType.Physical

  override def toString: String = s"${name}(${weaponType.toString}, ${damageType})"
}

object WeaponType extends Enumeration {
  type Type = Value
  val Scepter, Mace, Axe, Sword, Bow, Dagger, Staff, Wand, Polearm = Value
}

object DamageType extends Enumeration {
  type Type = Value
  val Physical, Fire, Cold, Lightning = Value
}

case class Sword(name: String,
                 cd: Int,
                 baseDamage: Int,
                 twoHanded: Boolean,
                 damageType: DamageType.Type = DamageType.Physical,
                 rarity: Rarity.Type = Rarity.Mediocre,
                 passiveEffects: List[PassiveEffect] = List.empty,
                 activeEffects: List[ActiveEffect] = List.empty) extends Weapon {
  override def weaponType: WeaponType.Type = WeaponType.Sword
}

case class Dagger(name: String,
                  cd: Int,
                  baseDamage: Int,
                  damageType: DamageType.Type = DamageType.Physical,
                  rarity: Rarity.Type = Rarity.Mediocre,
                  passiveEffects: List[PassiveEffect] = List.empty,
                  activeEffects: List[ActiveEffect] = List.empty) extends Weapon {
  override def weaponType: WeaponType.Type = WeaponType.Dagger

  override def twoHanded: Boolean = false
}

case class Polearm(name: String,
                   cd: Int,
                   baseDamage: Int,
                   twoHanded: Boolean,
                   damageType: DamageType.Type = DamageType.Physical,
                   rarity: Rarity.Type = Rarity.Mediocre,
                   passiveEffects: List[PassiveEffect] = List.empty,
                   activeEffects: List[ActiveEffect] = List.empty) extends Weapon {
  override def weaponType: WeaponType.Type = WeaponType.Polearm
}
