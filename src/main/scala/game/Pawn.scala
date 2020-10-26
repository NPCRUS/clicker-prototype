package game

import game.items._

case class InitialProperties(hp: Int = 100,
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
    val armorFromShield = handle.getShield match {
      case Some(s) => s.armor
      case _ => 0
    }

    initArmor + armorFromEffects + armorFromShield
  }
  lazy val parryRate: Int =
    getEffectChangeByType(EffectTargetType.Parry) + initProperties.parryRate
  lazy val evasionRate: Int =
    getEffectChangeByType(EffectTargetType.Evasion) + initProperties.evasionRate
  lazy val blockRate: Int =
    getEffectChangeByType(EffectTargetType.Block) + initProperties.blockRate
  lazy val coldResistance: Int =
    getEffectChangeByType(EffectTargetType.ColdRes) + initProperties.coldResistance
  lazy val fireResistance: Int =
    getEffectChangeByType(EffectTargetType.FireRes) + initProperties.fireResistance
  lazy val lightningResistance: Int =
    getEffectChangeByType(EffectTargetType.LightningRes) + initProperties.lightningResistance
  lazy val coldMitigation: Int =
    getEffectChangeByType(EffectTargetType.ColdMit) + initProperties.coldMitigation
  lazy val fireMitigation: Int =
    getEffectChangeByType(EffectTargetType.FireMit) + initProperties.fireMitigation
  lazy val lightningMitigation: Int =
    getEffectChangeByType(EffectTargetType.LightningMit) + initProperties.lightningMitigation
  lazy val armorMitigation: Int =
    getEffectChangeByType(EffectTargetType.ArmorMit) + initProperties.armorMitigation
  lazy val accuracyRating: Int =
    getEffectChangeByType(EffectTargetType.Accuracy) + initProperties.accuracyRating

  def getAllItems: List[Item] = handle.getWeapons ++ armorSet.getAll

  private def getEffectChangeByType(targetT: EffectTargetType.Type) =
    allPassiveEffects.filter(_.target == targetT).map(_.change).sum
}
