package game

import game.items._
import AttributesEffects._

object Opponent {
  def fromPawn(pawn: Pawn): Opponent = new Opponent(pawn, pawn.hp, List.empty)
}

case class Opponent(pawn: Pawn,
                    hp: Int,
                    activeEffects: List[ActiveEffect]) {

  // calculate output damage
  def calculateDamage(item: Weapon): Int = {
    val damage = masteryPhysical(item.getDamage, physicalMasteryRate)
    isCriticalDamage(damage, criticalChance, criticalMultiplier)
      .fold(damage)(v => v)
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

  def dealDamage(attacker: Opponent, damageType: DamageType.Type, damage: Int): (Int, Opponent) = {
    val actualDamage = damageType match {
      case DamageType.Physical => calculatePhysicalDamage(attacker, damage)
      case DamageType.Magical => calculateMagicalDamage(attacker, damage)
    }

    (actualDamage, Opponent(pawn, hp - actualDamage, activeEffects))
  }

  private def calculatePhysicalDamage(attacker: Opponent, damage: Int): Int =
    armorMitigation(damage, armor)

  private def calculateMagicalDamage(attacker: Opponent, damage: Int): Int =
    resistMagic(damage, magicalResistanceRate)

  def isEvaded(attacker: Opponent): Boolean =
    AttributesEffects.isEvaded(evasionRate, attacker.accuracyRate)

  def isParried: Boolean =
    // if weapon allow
    AttributesEffects.isParried(parryRate)

  def isBlocked: Boolean =
    // if weapon allow
    AttributesEffects.isBlocked(blockRate)

  def armor: Int =
    pawn.armor + getEffectChangeByType(EffectTargetType.Armor)

  def evasionRate: Int =
    pawn.evasionRate + getEffectChangeByType(EffectTargetType.Evasion)

  def parryRate: Int =
    pawn.parryRate + getEffectChangeByType(EffectTargetType.Parry)

  def blockRate: Int =
    pawn.blockRate + getEffectChangeByType(EffectTargetType.Block)

  def magicalResistanceRate: Int =
    pawn.magicalResistance + getEffectChangeByType(EffectTargetType.MagicResistance)

  def physicalMasteryRate: Int =
    pawn.physicalMastery + getEffectChangeByType(EffectTargetType.PhysicMastery)

  def criticalChance: Int =
    pawn.criticalChance + getEffectChangeByType(EffectTargetType.CritChance)

  def criticalMultiplier: Int =
    pawn.criticalMultiplier + getEffectChangeByType(EffectTargetType.CritMultiplier)

  def accuracyRate: Int =
    pawn.accuracyRating + getEffectChangeByType(EffectTargetType.Accuracy)

  private def getEffectChangeByType(targetT: EffectTargetType.Type) =
    activeEffects.filter(_.target == targetT).map(_.change).sum

  override def toString: String = s"${pawn.name}(${hp}hp)"
}
