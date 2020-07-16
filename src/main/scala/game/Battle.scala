package game

import scala.annotation.tailrec

class Battle(pawn1: Pawn, pawn2: Pawn) {
  val itemsCombined: List[CalcItem] =
    (pawn1.getAllActiveItems.map((pawn1, _)) ++ pawn2.getAllActiveItems.map((pawn2, _)))
    .map(t => CalcItem(t._1, t._2, t._2.cd))

  val opponents: (Opponent, Opponent) = (Opponent.fromPawn(pawn1), Opponent.fromPawn(pawn2))

  def calculate(): List[Action] = {
    @tailrec
    def _calculate(opponents: (Opponent, Opponent), actions: List[Action], items: List[CalcItem], timestamp: Int = 0): List[Action] = {
      val ordered = items.sortBy(_.cd)
      val currentCalc = ordered.head
      val currentItem = currentCalc.item

      if(opponents._1.hp <= 0 || opponents._2.hp <= 0) actions
      else {
        val actionOption = createAction(opponents, currentCalc, timestamp + currentCalc.cd)
        val newActions = {
          if(actionOption.isDefined) actions :+ actionOption.get
          else actions
        }
        val newOpponents = {
          if(actionOption.isDefined) (actionOption.get.init, actionOption.get.target)
          else opponents
        }

        _calculate(
          newOpponents,
          newActions,
          ordered.tail.map(t => t.withNewCd(t.cd - currentCalc.cd)) :+ currentCalc.withNewCd(currentItem.cd),
          timestamp + currentCalc.cd
        )
      }
      }

    _calculate(opponents, List.empty, itemsCombined)
  }

  private def createAction(op: (Opponent, Opponent), ci: CalcItem, timestamp: Int): Option[Action] = {
    ci.item match {
      case w: Weapon => Some(attack(op, ci, timestamp + ci.cd, w))
      case a: Armor =>
        println(s"we don't know how to process this type of item yet ${a}")
        None
    }
  }

  private def attack(op: (Opponent, Opponent), ci: CalcItem, timestamp: Int, weapon: Weapon): Attack = {
    val initTargetTuple = initTarget(op, ci)
    Attack(initTargetTuple._1, initTargetTuple._2, weapon, timestamp)
  }

  private def initTarget(op: (Opponent, Opponent), ci: CalcItem): (Opponent, Opponent) = {
    if(ci.pawn.equals(op._1.pawn)) {
      (op._1, op._2)
    } else {
      (op._2, op._1)
    }
  }
}

object Battle {
  def apply(o1: Pawn, o2: Pawn): Battle = new Battle(o1, o2)
}

case class CalcItem(pawn: Pawn, item: Item, cd: Int) {
  def withNewCd(newCd: Int) = CalcItem(pawn, item , newCd)
}