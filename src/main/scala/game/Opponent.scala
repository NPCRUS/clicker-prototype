package game

import game.items.{ActiveEffect, DamageType, EffectTargetType, Weapon}

import scala.util.Random

object Opponent {
  def fromPawn(pawn: Pawn): Opponent = new Opponent(pawn, pawn.hp, List.empty)
}

case class Opponent(pawn: Pawn,
                    hp: Int,
                    activeEffects: List[ActiveEffect]) {
  def calculateDamage(item: Weapon, baseDamage: Int): Int = {
    // logic for crits and any other sort of multipliers
    baseDamage
  }

  def removeActiveEffect(a: ActiveEffect): Opponent =
    Opponent(pawn, hp, activeEffects.filter(_ ne a))

  def applyActiveEffect(a: ActiveEffect): Opponent = {
    a.target match {
      case EffectTargetType.Hp => addHp(a.change)
      case _ => Opponent(pawn, hp, activeEffects :+ a)
    }
  }

  private def addHp(addition: Int): Opponent = {
    val newHp = hp + addition
    if (newHp < pawn.hp) Opponent(pawn, newHp, activeEffects)
    else Opponent(pawn, pawn.hp, activeEffects)
  }

  def isEvaded(attacker: Opponent): Boolean =
    (nextRand - (evasionRate - attacker.accuracyRating)) < 0

  def evasionRate: Double =
    statsConvert(pawn.evasionRate + getEffectChangeByType(EffectTargetType.Evasion))

  def accuracyRating: Double =
    statsConvert(pawn.accuracyRating + getEffectChangeByType(EffectTargetType.Accuracy))

  def isParried: Boolean = (nextRand - parryRate) < 0

  def parryRate: Double =
    statsConvert(pawn.parryRate + getEffectChangeByType(EffectTargetType.Parry))

  def isBlocked: Boolean =
    pawn.handle.getShield.isDefined && ((nextRand - blockRate) < 0)

  private def nextRand: Double = Random.between(0, 100).toDouble * 0.01

  def blockRate: Double =
    statsConvert(pawn.blockRate + getEffectChangeByType(EffectTargetType.Block))

  private def getEffectChangeByType(targetT: EffectTargetType.Type) =
    activeEffects.filter(_.target == targetT).map(_.change).sum

  private def statsConvert(inputInt: Int): Double = inputInt.toDouble * 0.0001

  def dealDamage(attacker: Opponent, damageType: DamageType.Type, damage: Int): (Int, Opponent) = {
    val actualDamage = damageType match {
      case DamageType.Physical => calculatePhysicalDamage(attacker, damage)
      case _ => calculateMagicalDamage(attacker, damageType, damage)
    }

    (actualDamage, Opponent(pawn, hp - actualDamage, activeEffects))
  }

  private def calculatePhysicalDamage(attacker: Opponent, damage: Int): Int = {
    val effectiveArmor = armor.toDouble * (1 - attacker.armorMitigation)
    val armorMitigation = effectiveArmor.toDouble / (effectiveArmor + 10 * damage)

    (damage * (1 - armorMitigation)).round.toInt
  }

  def armor: Int =
    pawn.armor + getEffectChangeByType(EffectTargetType.Armor)

  def armorMitigation: Double = ???

  private def calculateMagicalDamage(attacker: Opponent, damageType: DamageType.Type, damage: Int): Int = ???

  // mitigation + mastery technique

  override def toString: String = s"${pawn.name}(${hp}hp)"
}
