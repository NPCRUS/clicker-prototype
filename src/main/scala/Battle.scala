import scala.annotation.tailrec

object Battle {
  def apply(o1: Pawn, o2: Pawn): Battle = new Battle(o1, o2)
}

class Battle(opponent1: Pawn, opponent2: Pawn) {
  val itemsCombined: List[(Item, Int)] =
    for(item <- opponent1.items() ++ opponent2.items()) yield (item, item.cd())

  def calculate(): List[Action] = {
    @tailrec
    def _calculate(actions: List[Action], items: List[(Item, Int)], timestamp: Int = 0): List[Action] = {
      val ordered = items.sortBy(_._2)
      val currentAction = ordered.head
      val currentItem = currentAction._1

      if(actions.length == 25) actions
      else _calculate(
        actions :+ new Action(currentItem, timestamp + currentAction._2),
        ordered.tail.map(t => (t._1, t._2 - currentAction._2)) :+ (currentItem, currentItem.cd()),
        timestamp + currentAction._2
      )
    }

    _calculate(List.empty, itemsCombined)
  }
}
