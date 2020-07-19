package game

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

  def addHp(addition: Int): Opponent = {
    val newHp = hp + addition
    if(newHp < pawn.hp) Opponent(pawn, newHp)
    else Opponent(pawn, pawn.hp)
  }

  private def nextRand: Double = Random.between(0, 100).toDouble * 0.01

  def isEvaded(attacker: Opponent): Boolean =
    (nextRand - (pawn.evasionRate - attacker.pawn.accuracyRating)) < 0

  def isParried: Boolean = (nextRand - pawn.parryRate) < 0
  def isBlocked: Boolean =
    pawn.handle.getShield.isDefined && ((nextRand - pawn.blockRate) < 0)

  def dealDamage(attacker: Opponent, damageType: DamageType.DamageType, damage: Int): (Int, Opponent) = {
    val actualDamage = damageType match {
      case DamageType.Physical => calculatePhysicalDamage(attacker, damage)
      case _ => calculateMagicalDamage(attacker, damageType, damage)
    }

    (actualDamage, new Opponent(pawn, hp - actualDamage))
  }

  private def calculatePhysicalDamage(attacker: Opponent, damage: Int): Int = {
    val effectiveArmor = pawn.armor.toDouble * (1 - attacker.pawn.armorMitigation)
    val armorMitigation = effectiveArmor.toDouble / (effectiveArmor + 10 * damage)

    (damage * (1 - armorMitigation)).round.toInt
  }

  private def calculateMagicalDamage(attacker: Opponent, damageType: DamageType.DamageType, damage: Int): Int = {
    val (magicalResist, magicalMitigation) = damageType match {
      case DamageType.Cold => (pawn.coldResistance, attacker.pawn.coldMitigation)
      case DamageType.Fire => (pawn.fireResistance, attacker.pawn.fireMitigation)
      case DamageType.Lightning => (pawn.lightningResistance, attacker.pawn.lightningMitigation)
    }

    val resist = (magicalResist - magicalMitigation)
    if(resist <= 0) damage
    else (damage - damage * resist).round.toInt
  }

  override def toString: String = s"${pawn.name}(${hp}hp)"
}
