package game

import game.items._

case class InitialProperties(hp: Int = 100,
                             parryRate: Int = 0,
                             evasionRate: Int = 0,
                             blockRate: Int = 0,
                             physicalMitigation: Int = 0,
                             magicMitigation: Int = 0,
                             physicalMastery: Int = 0,
                             magicalMastery: Int = 0,
                             criticalChance: Int = 0,
                             criticalMultiplier: Int = 0,
                             accuracyRating: Int = 0)

case class Pawn(name: String,
                handle: Handle,
                armorSet: ArmorSet,
                initProperties: InitialProperties) {

  lazy val allPassiveEffects: List[PassiveEffect] =
    getAllItems.map(_.passiveEffects).reduce((c, acc) => acc ++ c)

  lazy val hp: Int = getEffectChangeByType(EffectTargetType.Hp) + initProperties.hp

  lazy val armor: Int = {
    val initArmor = armorSet.getAll.map(_.armor).sum
    val armorFromEffects = getEffectChangeByType(EffectTargetType.Armor)
    val armorFromShield = handle.getShield.fold(0)(_.armor)

    initArmor + armorFromEffects + armorFromShield
  }

  lazy val parryRate: Int =
    getEffectChangeByType(EffectTargetType.Parry) + initProperties.parryRate
  lazy val evasionRate: Int =
    getEffectChangeByType(EffectTargetType.Evasion) + initProperties.evasionRate
  lazy val blockRate: Int =
    getEffectChangeByType(EffectTargetType.Block) + initProperties.blockRate
  lazy val physicalMitigation: Int =
    getEffectChangeByType(EffectTargetType.PhysicMit) + initProperties.physicalMitigation
  lazy val magicalMitigation: Int =
    getEffectChangeByType(EffectTargetType.MagicMit) + initProperties.magicMitigation
  lazy val physicalMastery: Int =
    getEffectChangeByType(EffectTargetType.PhysicMastery) + initProperties.physicalMastery
  lazy val magicalMastery: Int =
    getEffectChangeByType(EffectTargetType.MagicMastery) + initProperties.magicalMastery
  lazy val criticalChance: Int =
    getEffectChangeByType(EffectTargetType.CritChance) + initProperties.criticalChance
  lazy val criticalMultiplier: Int =
    getEffectChangeByType(EffectTargetType.CritMultiplier) + initProperties.criticalMultiplier
  lazy val accuracyRating: Int =
    getEffectChangeByType(EffectTargetType.Accuracy) + initProperties.accuracyRating

  def getAllItems: List[Item] = handle.getWeapons ++ armorSet.getAll

  private[game] def getEffectChangeByType(targetT: EffectTargetType.Type): Int =
    allPassiveEffects.filter(_.target == targetT).map(_.change).sum
}
