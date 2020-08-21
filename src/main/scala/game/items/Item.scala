package game.items

import game.items.ActiveEffectType.Type

import scala.util.Random

trait Item {
  def name: String
  def cd: Int
  def _type: ItemType.Type
  def passiveEffects: List[PassiveEffect]
  def activeEffects: List[ActiveEffect]
}

object ItemType extends Enumeration {
  type Type = Value
  val Weapon, Armor = Value
}

object EffectTargetType extends Enumeration {
  type Type = Value
  val Hp, Armor, Parry, Evasion, Block, ColdRes, FireRes, LightningRes, ColdMit, FireMit, LightningMit, ArmorMit, Accuracy = Value
}

case class PassiveEffect(
  target: EffectTargetType.Type ,
  change: Int
)

object ActiveEffectType extends Enumeration {
  type Type = Value
  val OneTime, Periodic, Lasting = Value
}

trait ActiveEffect {
  def _type: ActiveEffectType.Type
  def target: EffectTargetType.Type
  def chance: Double
  def change: Int
  def self: Boolean

  def invoke: Boolean = ((Random.between(0, 100).toDouble * 0.01) - chance) < 0
}

case class OneTimeActiveEffect(
  target: EffectTargetType.Type,
  chance: Double,
  change: Int,
  self: Boolean
) extends ActiveEffect {
  override def _type: Type = ActiveEffectType.OneTime
}

case class PeriodicActiveEffect(
  target: EffectTargetType.Type,
  chance: Double,
  change: Int,
  self: Boolean,
  ticks: Int,
  tickCd: Int
) extends ActiveEffect {
  override def _type: Type = ActiveEffectType.Periodic
}

case class LastingActiveEffect(
  target: EffectTargetType.Type,
  chance: Double,
  change: Int,
  self: Boolean,
  duration: Int
) extends ActiveEffect {
  override def _type: Type = ActiveEffectType.Lasting
}

