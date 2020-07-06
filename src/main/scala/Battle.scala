import scala.annotation.tailrec

object Battle {
  def apply(o1: Pawn, o2: Pawn): Battle = new Battle(o1, o2)
}

class Battle(opponent1: Pawn, opponent2: Pawn) {
  val itemsCombined: List[CalcItem] =
    for(comb <- opponent1.items.map((_, opponent1)) ++ opponent2.items.map((_, opponent2)))
      yield CalcItem(comb._2, comb._1, comb._1.cd)

  val opponents: (Opponent, Opponent) = (Opponent(opponent1), Opponent(opponent2))

  def calculate(): List[Action] = {
    @tailrec
    def _calculate(opponents: (Opponent, Opponent), actions: List[Action], items: List[CalcItem], timestamp: Int = 0): List[Action] = {
      val ordered = items.sortBy(_.cd)
      val currentCalc = ordered.head
      val currentItem = currentCalc.item

      if(opponents._1.hp <= 0 || opponents._2.hp <= 0) actions
      else {
        val action: Action = createAction(opponents, currentCalc, timestamp + currentCalc.cd)
        _calculate(
          (action.init, action.target),
          actions :+ action,
          ordered.tail.map(t => t.withNewCd(t.cd - currentCalc.cd)) :+ currentCalc.withNewCd(currentItem.cd),
          timestamp + currentCalc.cd
        )
      }
    }

    _calculate(opponents, List.empty, itemsCombined)
  }

  private def createAction(op: (Opponent, Opponent), ci: CalcItem, timestamp: Int): Action = {
    ci.item match {
      case w: Weapon => attack(op, ci, timestamp + ci.cd, w)
    }
  }

  private def attack(op: (Opponent, Opponent), ci: CalcItem, timestamp: Int, weapon: Weapon): Attack = {
    val initTargetTuple = initTarget(op, ci)
    Attack(initTargetTuple._1, initTargetTuple._2, weapon, timestamp, weapon.damage)
  }

  private def initTarget(op: (Opponent, Opponent), ci: CalcItem): (Opponent, Opponent) = {
    if(ci.pawn.equals(op._1.pawn)) {
      (op._1, op._2)
    } else {
      (op._2, op._1)
    }
  }
}

case class CalcItem(pawn: Pawn, item: Item, cd: Int) {
  def withNewCd(newCd: Int) = CalcItem(pawn, item , newCd)
}
