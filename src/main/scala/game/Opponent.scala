package game

import game.EffectTargetType.EffectTargetType

import scala.util.Random

object Opponent {
  def apply(pawn: Pawn, hp: Int): Opponent = new Opponent(pawn, hp)
  def fromPawn(pawn: Pawn): Opponent = new Opponent(pawn, pawn.hp)
}

case class Opponent(
  pawn: Pawn,
  hp: Int,
  activeEffects: List[ActiveEffect] = List.empty
) {
  def calculateDamage(item: Weapon, baseDamage: Int): Int = {
    // logic for crits and any other sort of multipliers
    baseDamage
  }

  def removeActiveEffect(a: ActiveEffect): Opponent =
    Opponent(pawn, hp, activeEffects.filter(_ ne a))

  def applyActiveEffect(a: ActiveEffect): Opponent =
    Opponent(pawn, hp, activeEffects :+ a)

  def addHp(addition: Int): Opponent = {
    val newHp = hp + addition
    if(newHp < pawn.hp) Opponent(pawn, newHp)
    else Opponent(pawn, pawn.hp)
  }

  private def nextRand: Double = Random.between(0, 100).toDouble * 0.01

  def isEvaded(attacker: Opponent): Boolean =
    (nextRand - (evasionRate - attacker.accuracyRating)) < 0

  def isParried: Boolean = (nextRand - parryRate) < 0
  def isBlocked: Boolean =
    pawn.handle.getShield.isDefined && ((nextRand - blockRate) < 0)

  def dealDamage(attacker: Opponent, damageType: DamageType.DamageType, damage: Int): (Int, Opponent) = {
    val actualDamage = damageType match {
      case DamageType.Physical => calculatePhysicalDamage(attacker, damage)
      case _ => calculateMagicalDamage(attacker, damageType, damage)
    }

    (actualDamage, new Opponent(pawn, hp - actualDamage))
  }

  private def calculatePhysicalDamage(attacker: Opponent, damage: Int): Int = {
    val effectiveArmor = armor.toDouble * (1 - attacker.armorMitigation)
    val armorMitigation = effectiveArmor.toDouble / (effectiveArmor + 10 * damage)

    (damage * (1 - armorMitigation)).round.toInt
  }

  private def calculateMagicalDamage(attacker: Opponent, damageType: DamageType.DamageType, damage: Int): Int = {
    val (magicalResist, magicalMitigation) = damageType match {
      case DamageType.Cold => (coldResistance, attacker.coldMitigation)
      case DamageType.Fire => (fireResistance, attacker.fireMitigation)
      case DamageType.Lightning => (lightningResistance, attacker.lightningMitigation)
    }

    val resist = (magicalResist - magicalMitigation)
    if(resist <= 0) damage
    else (damage - damage * resist).round.toInt
  }

  private def getEffectChangeByType(targetT: EffectTargetType) =
    activeEffects.filter(_.target == targetT).map(_.change).sum

  private def statsConvert(inputInt: Int): Double = inputInt.toDouble * 0.0001

  def armor: Int =
    pawn.armor + getEffectChangeByType(EffectTargetType.Armor)
  def parryRate: Double =
    statsConvert(pawn.parryRate + getEffectChangeByType(EffectTargetType.Parry))
  def evasionRate: Double =
    statsConvert(pawn.evasionRate + getEffectChangeByType(EffectTargetType.Evasion))
  def blockRate: Double =
    statsConvert(pawn.blockRate + getEffectChangeByType(EffectTargetType.Block))
  def coldResistance: Double =
    statsConvert(pawn.coldResistance + getEffectChangeByType(EffectTargetType.ColdRes))
  def fireResistance: Double =
    statsConvert(pawn.fireResistance + getEffectChangeByType(EffectTargetType.FireRes))
  def lightningResistance: Double =
    statsConvert(pawn.lightningResistance + getEffectChangeByType(EffectTargetType.LightningRes))
  def coldMitigation: Double =
    statsConvert(pawn.coldMitigation + getEffectChangeByType(EffectTargetType.ColdMit))
  def fireMitigation: Double =
    statsConvert(pawn.fireMitigation + getEffectChangeByType(EffectTargetType.FireMit))
  def lightningMitigation: Double =
    statsConvert(pawn.lightningMitigation + getEffectChangeByType(EffectTargetType.LightningMit))
  def armorMitigation: Double =
    statsConvert(pawn.armorMitigation + getEffectChangeByType(EffectTargetType.ArmorMit))
  def accuracyRating: Double =
    statsConvert(pawn.accuracyRating + getEffectChangeByType(EffectTargetType.Accuracy))

  override def toString: String = s"${pawn.name}(${hp}hp)"
}
