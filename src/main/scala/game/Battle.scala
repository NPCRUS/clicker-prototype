package game

import scala.annotation.tailrec

object Battle {
  def apply(o1: Pawn, o2: Pawn): Battle = new Battle(o1, o2)
}

class Battle(pawn1: Pawn, pawn2: Pawn) {
  val itemsCombined: List[Applicable] =
    (pawn1.getAllItems.map((pawn1, _)) ++ pawn2.getAllItems.map((pawn2, _)))
    .map(t => ItemApplicable(t._1, t._2, t._2.cd))

  val opponents: Opponents = Opponents(Opponent.fromPawn(pawn1), Opponent.fromPawn(pawn2))

  def calculate(): List[Action] = {
    @tailrec
    def _calculate(opponents: Opponents, actions: List[Action], applicable: List[Applicable], timestamp: Int = 0): List[Action] = {
      val ordered = applicable.sortBy(_.cd)
      val currentApplicable = ordered.head

      if(opponents.init.hp <= 0 || opponents.target.hp <= 0) actions
      // else if(actions.length > 10) actions
      else {
        val applicableResult = processApplicable(opponents, currentApplicable, timestamp + currentApplicable.cd)

        val newOpponents = applicableResult.opponents

        // work with
        val preparedApplicable = ordered.tail.map(t => t.withNewCd(t.cd - currentApplicable.cd))
        val applicableApplied = currentApplicable match {
          case p: PeriodicEffectApplicable if(p.ticks > 1) => preparedApplicable :+ p.doTick
          case l: LastingEffectApplicable => preparedApplicable
          case o: OneTimeEffectApplicable => preparedApplicable
          case i: ItemApplicable => preparedApplicable :+ i.withNewCd(i.item.cd)
        }

        println()
        _calculate(
          newOpponents,
          actions ++ applicableResult.actions,
          applicableApplied ++ applicableResult.subApplicable,
          timestamp + currentApplicable.cd
        )
      }
    }

    _calculate(opponents, List.empty, itemsCombined)
  }

  private def processApplicable(op: Opponents, a: Applicable, timestamp: Int): ApplicableResult = {
    val orderedOp = orderOpByApplicable(op, a)
    a match {
      case i: ItemApplicable => processItem(orderedOp, i, timestamp)
      case p: PeriodicEffectApplicable => processPeriodic(orderedOp, p, timestamp)
      case l: LastingEffectApplicable => processLasting(orderedOp, l, timestamp)
      case o: OneTimeEffectApplicable => processOneTime(orderedOp, o, timestamp)
    }
  }

  private def processItem(op: Opponents, a: ItemApplicable, timestamp: Int): ApplicableResult = {
    val attackOption = a.item match {
      case w: Weapon => Some(Attack(op.init, op.target, w, timestamp))
      case _ => None
    }

    val actualOp = attackOption match {
      case Some(a) => Opponents(a.init, a.target)
      case None => op
    }

    val effects = a.item.activeEffects.filter(_.invoke) // calculate if effect can be applied according to chance
    val newApplicable = effects
      .map {
      case p: PeriodicActiveEffect => PeriodicEffectApplicable(a.pawn, a.item, p, p.ticks, p.tickCd, 0)
      case l: LastingActiveEffect => LastingEffectApplicable(a.pawn, a.item, l, 0)
      case o: OneTimeActiveEffect => OneTimeEffectApplicable(a.pawn, a.item, o, 0)
    }

    val actions = if(attackOption.isDefined) List(attackOption.get)
    else List.empty

    ApplicableResult(
      actions,
      actualOp,
      newApplicable
    )
  }

//  private def applyEffects(
//    af: List[ActiveEffect],
//    initOp: Opponents,
//    timestamp: Int,
//    actions: List[Option[Action]] = List.empty
//  ): (List[Option[Action]], Opponents) = {
//    if(af.isEmpty) (actions, initOp)
//    else {
//      val effect = af.head
//      val newAction = Effect(initOp.init, initOp.target, effect, timestamp)
//      val newOp = chooseNewerOp(newAction, initOp)
//      applyEffects(af.tail, newOp, timestamp, actions :+ Some(newAction))
//    }
//  }

  private def processPeriodic(op: Opponents, a: PeriodicEffectApplicable, timestamp: Int): ApplicableResult = ???

  private def processLasting(op: Opponents, a: LastingEffectApplicable, timestamp: Int): ApplicableResult = ???

  private def processOneTime(op: Opponents, a: OneTimeEffectApplicable, timestamp: Int): ApplicableResult = {
    // choose target
    val target = if(a.activeEffect.self) op.init else op.target

    a.activeEffect.target match {
      case EffectTargetType.Hp => {
        val newTarget = target.addHp(a.activeEffect.change)
        val action = OneTimeEffectApplication(op.init, newTarget, a.source, timestamp, a.activeEffect)
        ApplicableResult(
          List(action),
          chooseNewerOp(action, op),
          List.empty
        )
      }
    }
  }

  private def orderOpByApplicable(lastOp: Opponents, ci: Applicable): Opponents =
    if(ci.pawn.equals(lastOp.init.pawn)) lastOp else Opponents(lastOp.target, lastOp.init)

  private def chooseNewerOp(action: Action, lastOp: Opponents): Opponents =
    if(action.init.pawn equals action.target.pawn) Opponents(action.target, lastOp.target)
    else Opponents(action.init, action.target)
}

trait Applicable {
  def pawn: Pawn
  def cd: Int

  def withNewCd(newCd: Int): Applicable
}

case class ItemApplicable(pawn: Pawn, item: Item, cd: Int)
  extends Applicable {
  override def withNewCd(newCd: Int): Applicable = ItemApplicable(pawn, item , newCd)
}

case class OneTimeEffectApplicable(pawn: Pawn, source: Item, activeEffect: OneTimeActiveEffect, cd: Int)
  extends Applicable {
  override def withNewCd(newCd: Int): Applicable = OneTimeEffectApplicable(pawn, source, activeEffect, newCd)
}

case class PeriodicEffectApplicable(pawn: Pawn, source: Item, activeEffect: PeriodicActiveEffect, ticks: Int, tickCd: Int, cd: Int)
  extends Applicable {
  override def withNewCd(newCd: Int): Applicable = PeriodicEffectApplicable(pawn, source, activeEffect, ticks, tickCd, newCd)
  def doTick: PeriodicEffectApplicable = PeriodicEffectApplicable(pawn, source, activeEffect, ticks - 1, tickCd, tickCd)
}

case class LastingEffectApplicable(pawn: Pawn, source: Item, activeEffect: LastingActiveEffect, cd: Int)
  extends Applicable {
  override def withNewCd(newCd: Int): Applicable = LastingEffectApplicable(pawn, source, activeEffect, newCd)
}

case class ApplicableResult(
  actions: List[Action],
  opponents: Opponents,
  subApplicable: List[Applicable]
)

case class Opponents(
  init: Opponent,
  target: Opponent
)