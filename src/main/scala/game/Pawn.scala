package game

import game.EffectTargetType.EffectTargetType

case class InitialProperties(
  hp: Int = 100,
  parryRate: Int = 0,
  evasionRate: Int = 0,
  blockRate: Int = 0,
  coldResistance: Int = 0,
  fireResistance: Int = 0,
  lightningResistance: Int = 0,
  coldMitigation: Int = 0,
  fireMitigation: Int = 0,
  lightningMitigation: Int = 0,
  armorMitigation: Int = 0,
  accuracyRating: Int = 0
)

case class Pawn(
  name: String,
  handle: Handle,
  armorSet: ArmorSet,
  initProperties: InitialProperties
) {
  def getAllItems: List[Item] = handle.getWeapons ++ armorSet.getAll
  lazy val allPassiveEffects: List[PassiveEffect] =
    getAllItems.map(_.passiveEffects).reduce((c, acc) => acc ++ c)

  private def getEffectChangeByType(targetT: EffectTargetType) =
    allPassiveEffects.filter(_.target == targetT).map(_.change).sum

  private def statsConvert(inputInt: Int): Double = inputInt.toDouble * 0.0001

  lazy val hp: Int = getEffectChangeByType(EffectTargetType.Hp) + initProperties.hp
  lazy val armor: Int = {
    val initArmor = armorSet.getAll.map(_.armor).sum
    val armorFromEffects = getEffectChangeByType(EffectTargetType.Armor)
    val armorFromShield = handle.getShield match {
      case Some(s) => s.armor
      case _ => 0
    }

    initArmor + armorFromEffects + armorFromShield
  }
  lazy val parryRate: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.Parry) + initProperties.parryRate)
  lazy val evasionRate: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.Evasion) + initProperties.evasionRate)
  lazy val blockRate: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.Block) + initProperties.blockRate)
  lazy val coldResistance: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.ColdRes) + initProperties.coldResistance)
  lazy val fireResistance: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.FireRes) + initProperties.fireResistance)
  lazy val lightningResistance: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.LightningRes) + initProperties.lightningResistance)
  lazy val coldMitigation: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.ColdMit) + initProperties.coldMitigation)
  lazy val fireMitigation: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.FireMit) + initProperties.fireMitigation)
  lazy val lightningMitigation: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.LightningMit) + initProperties.lightningMitigation)
  lazy val armorMitigation: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.ArmorMit) + initProperties.armorMitigation)
  lazy val accuracyRating: Double =
    statsConvert(getEffectChangeByType(EffectTargetType.Accuracy) + initProperties.accuracyRating)
}
