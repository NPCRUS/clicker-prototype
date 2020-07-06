import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BattleSpec extends AnyWordSpecLike with Matchers {
  val knife = new Weapon("knife", false, 920, 2)
  val pitchfork = new Weapon("pitchfork", true, 2300,5)

  val pawn1: Pawn = Pawn("Mate", 40, List(knife), ArmorSet.empty)
  val pawn2: Pawn = Pawn("John", 40, List(pitchfork), ArmorSet.empty)

  "battle" should {
    "exit with list of actions if one of the opponents has 0 hp" in {
      val actions = Battle(pawn1, Pawn("test", 0, List.empty, ArmorSet.empty)).calculate()
      actions.length shouldEqual 0
    }

    "exit with at least one action if both of opponents has more than 0 hp" in {
      val actions = Battle(pawn1, pawn2).calculate()
      actions.length should be > 0
    }
  }
}
