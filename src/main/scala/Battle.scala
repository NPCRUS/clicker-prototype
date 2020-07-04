import scala.annotation.tailrec

object Battle {
  def apply(o1: Pawn, o2: Pawn): Battle = new Battle(o1, o2)
}

class Battle(opponent1: Pawn, opponent2: Pawn) {
  val itemsCombined: List[CalcItem] =
    for(comb <- opponent1.items.map((_, opponent1)) ++ opponent2.items.map((_, opponent2)))
      yield CalcItem(comb._2, comb._1, comb._1.cd)

  def calculate(): List[Action] = {
    @tailrec
    def _calculate(actions: List[Action], items: List[CalcItem], timestamp: Int = 0): List[Action] = {
      val ordered = items.sortBy(_.cd)
      val currentCalc = ordered.head
      val currentItem = currentCalc.item

      if(actions.length == 25) actions
      else _calculate(
        actions :+ createAction(currentCalc, timestamp + currentCalc.cd),
        ordered.tail.map(t => t.withNewCd(t.cd - currentCalc.cd)) :+ currentCalc.withNewCd(currentItem.cd),
        timestamp + currentCalc.cd
      )
    }

    _calculate(List.empty, itemsCombined)
  }

  private def createAction(ci: CalcItem, timestamp: Int): Action = {
    ci.item match {
      case w: Weapon => Attack(ci.pawn, ci.item, timestamp, w.damage)
    }
  }
}

case class CalcItem(pawn: Pawn, item: Item, cd: Int) {
  def withNewCd(newCd: Int) = CalcItem(pawn, item , newCd)
}
