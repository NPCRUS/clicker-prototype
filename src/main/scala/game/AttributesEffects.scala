package game

import scala.util.Random

object AttributesEffects {

  def happen(chance: Int): Boolean = {
    assert(chance > -1 && chance < 101)
    (Random.between(0, 100) - chance) < 0
  }

  // calculate critical damage if it happens
  // every 100 points of critical chance converts to 1% chance
  // every 100 points of critical damage multiplier adds 10% to base 100% multiplication
  def isCriticalDamage(baseDamage: Int, criticalChance: Int, criticalMultiplier: Int): Option[Int] =
    if(happen(criticalChance / 100))
      Some(baseDamage + baseDamage * 100 + (baseDamage * criticalMultiplier / 10))
    else
      None

  // every 100 points of parry rate converts to 1% chance
  def isParried(parryRate: Int): Boolean =
    happen(parryRate / 100)

  // every 100 points of evasion rate converts to 1% chance
  def isEvaded(evasionRate: Int, enemyAccuracyRate: Int): Boolean =
    happen((evasionRate - enemyAccuracyRate) / 100)

  // every 100 points of block rate converts to 1% chance
  def isBlocked(blockRate: Int): Boolean =
    happen(blockRate / 100)

  // every 100 points of magic resistance resist 1% of damage
  def resistMagic(baseDamage: Int, magicalResistance: Int): Int =
    baseDamage - baseDamage * (magicalResistance / 10000)

  // every 100 points of magic mastery increase 1% damage
  def masteryMagic(baseDamage: Int, magicMastery: Int): Int =
    baseDamage + baseDamage * (magicMastery / 10000)

  // every 100 points of armor decrease damage by 1%
  def armorMitigation(baseDamage: Int, armor: Int): Int =
    baseDamage - baseDamage * (armor / 10000)

  // every 100 points of physical mastery increase 1% damage
  def masteryPhysical(baseDamage: Int, physicalMastery: Int): Int =
    baseDamage + baseDamage * (physicalMastery / 10000)
}
