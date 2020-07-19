package game

import scala.util.Random

trait Item {
  def name: String
  def cd: Int
  def _type: String
  def passiveEffects: List[PassiveEffect]
  def activeEffects: List[ActiveEffect]
}

object EffectTargetType extends Enumeration {
  type EffectTargetType = Value
  val Hp, Armor, Parry, Evasion, Block, ColdRes, FireRes, LightningRes, ColdMit, FireMit, LightningMit, ArmorMit, Accuracy = Value
}

case class PassiveEffect(
  target: EffectTargetType.EffectTargetType ,
  change: Int
)

object ActiveEffectType extends Enumeration {
  type ActiveEffectType = Value
  val OneTime, Periodic, Lasting = Value
}

trait ActiveEffect {
  def _type: ActiveEffectType.ActiveEffectType
  def target: EffectTargetType.EffectTargetType
  def chance: Double
  def change: Int
  def self: Boolean

  def invoke: Boolean = ((Random.between(0, 100).toDouble * 0.01) - chance) < 0
}

case class OneTimeActiveEffect(
  _type: ActiveEffectType.ActiveEffectType,
  target: EffectTargetType.EffectTargetType,
  chance: Double,
  change: Int,
  self: Boolean
) extends ActiveEffect

case class PeriodicActiveEffect(
  _type: ActiveEffectType.ActiveEffectType,
  target: EffectTargetType.EffectTargetType,
  chance: Double,
  change: Int,
  self: Boolean,
  ticks: Int,
  tickCd: Int
) extends ActiveEffect

case class LastingActiveEffect(
  _type: ActiveEffectType.ActiveEffectType,
  target: EffectTargetType.EffectTargetType,
  chance: Double,
  change: Int,
  self: Boolean,
  duration: Int
) extends ActiveEffect

