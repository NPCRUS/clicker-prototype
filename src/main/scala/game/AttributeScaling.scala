package game

import game.items._
import game.items.EffectTargetType._

import scala.util.Random

object AttributeScaling {

  def calcItemCd(level: Int): Int =
    (-160 * level) + 8000

  def calcArmor(level: Int, armorType: ArmorPart.Type): Int =
    if(armorType == ArmorPart.Body || armorType == ArmorPart.Shield)
      (level * 36) + Random.between(10, 30)
    else
      (level * 15) + Random.between(10, 30)

  def calcCdAndDamage(level: Int, weaponType: WeaponType.Type): (Int, Int) = weaponType match {
    case WeaponType.Sword =>
      (
        (-15 * level) + 5000,
        7 + level * 20
      )
    case WeaponType.Dagger =>
      (
        (-15 * level) + 4000,
        4 + level * 20
      )
    case WeaponType.Polearm =>
      (
        (-15 * level) + 4500,
        6 + level * 20
      )
    case WeaponType.Axe =>
      (
        (-15 * level) + 5300,
        7 + level * 20
      )

  }

  def twoHandedConversion(twoHanded: Boolean, cd: Int, damage: Int): (Int, Int) =
    if(twoHanded)
      ((cd / 1.4).toInt, (damage * 1.4).toInt)
    else (cd, damage)

  def effect(level: Int, effectType: EffectTargetType.Type): PassiveEffect = effectType match {
    case Hp => PassiveEffect(Hp, 10 + 5 * level)
    case _ => PassiveEffect(effectType, 10 + level * 10)
  }

  def getEffectsByWeaponType(level: Int, weaponType: WeaponType.Type): List[PassiveEffect] = weaponType match {
    case WeaponType.Sword => List(
      effect(level, Parry),
      effect(level, PhysicMastery),
      effect(level, Accuracy),
      effect(level, EffectTargetType.Armor)
    )
    case WeaponType.Dagger => List(
      effect(level, Evasion),
      effect(level, CritMultiplier),
      effect(level, CritChance),
      effect(level, PhysicMastery)
    )
    case WeaponType.Polearm => List(
      effect(level, Parry),
      effect(level, PhysicMastery),
      effect(level, Accuracy),
      effect(level, CritChance)
    )
    case WeaponType.Axe => List(
      effect(level, PhysicMastery),
      effect(level, CritMultiplier),
      effect(level, CritChance),
      effect(level, Accuracy)
    )
  }

  def getEffectsForArmor(level: Int,
                         armorType: ArmorType.Type,
                         armorPart: ArmorPart.Type): List[PassiveEffect] = (armorType, armorPart) match {
    case (_, ArmorPart.Shield) => List(
      effect(level, Block),
      effect(level, Hp),
      effect(level, EffectTargetType.Armor),
      effect(level, Parry),
      effect(level, Evasion)
    )
    case (_, ArmorPart.Amulet) => EffectTargetType.values.toList.map(effect(level, _))
    case (ArmorType.Heavy, _) => List(
      effect(level, Hp),
      effect(level, Block),
      effect(level, Parry)
    )
    case (ArmorType.Medium, _) => List(
      effect(level, Accuracy),
      effect(level, PhysicMastery),
      effect(level, Parry)
    )
    case (ArmorType.Light, _) => List(
      effect(level, Evasion),
      effect(level, CritChance),
      effect(level, CritMultiplier)
    )
    case _ => throw new Exception("getEffectsForArmor match somehow not exhaustive")
  }
}
