package game

import game.ActionType.Type
import game.items._

trait Action {
  def init: Opponent

  def target: Opponent

  def item: Item

  def timestamp: Int

  def _type: ActionType.Type

  def timestampStr: String = s"${timestamp.toDouble / 1000}s"
}

object ActionType extends Enumeration {
  type Type = Value
  val DamageDeal, Avoidance, EffectApplication, EffectEnd = Value
}

object Attack {
  def apply(init: Opponent, target: Opponent, item: Weapon, timestamp: Int): Action = {
    if (target.isEvaded(init)) {
      Avoidance(init, target, item, timestamp, AvoidanceType.Evasion)
    } else if (item.isPhysical && target.isParried) {
      Avoidance(init, target, item, timestamp, AvoidanceType.Parry)
    } else if (item.isPhysical && target.isBlocked) {
      Avoidance(init, target, item, timestamp, AvoidanceType.Block)
    } else {
      val initialInputDamage = init.calculateDamage(item)
      val (finalDamage, newTarget) = target.dealDamage(init, item.damageType, initialInputDamage)
      DamageDeal(init, newTarget, item, timestamp, finalDamage)
    }
  }
}

case class DamageDeal(init: Opponent,
                      target: Opponent,
                      item: Item,
                      timestamp: Int,
                      damage: Int) extends Action {
  override def _type: Type = ActionType.DamageDeal

  override def toString: String =
    s"${init.toString} inflicted ${damage} damage on ${target.toString} with ${item.toString}, ${timestampStr}"
}

object AvoidanceType extends Enumeration {
  type Type = Value
  val Evasion, Parry, Block = Value
}

case class Avoidance(init: Opponent,
                     target: Opponent,
                     item: Item,
                     timestamp: Int,
                     avoidanceType: AvoidanceType.Type) extends Action {
  override def _type: Type = ActionType.Avoidance

  override def toString: String =
    s"${target.toString} performed an ${avoidanceType.toString} of attack from ${init.toString}, ${timestampStr}"
}

case class EffectApplication(init: Opponent,
                             target: Opponent,
                             item: Item,
                             timestamp: Int,
                             effect: ActiveEffect) extends Action {
  override def _type: Type = ActionType.EffectApplication

  override def toString: String = {
    val e = effect match {
      case o: OneTimeActiveEffect => s"onetime ${o.change} ${o.target.toString}"
      case l: LastingActiveEffect => s"${l.change} ${l.target.toString} for ${l.duration}"
      case p: PeriodicActiveEffect =>
        val ticksAmount = target.activeEffects.count(_ equals effect)
        s"${p.change} ${p.target.toString} for ${ticksAmount}/${p.ticks} ticks overall ${ticksAmount * p.change} ${p.target.toString}"
    }
    s"${init.toString} applied ${e} to ${target.toString}, ${timestampStr}"
  }
}

case class EffectEnd(init: Opponent,
                     target: Opponent,
                     item: Item,
                     timestamp: Int,
                     effect: ActiveEffect) extends Action {
  override def _type: Type = ActionType.EffectEnd

  override def toString: String =
    s"${effect._type}(${effect.change} ${effect.target.toString}) is ended on ${target.toString}, ${timestampStr}"
}


