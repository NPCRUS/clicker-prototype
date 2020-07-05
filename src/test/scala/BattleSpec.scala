import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BattleSpec extends AnyWordSpecLike with Matchers {
  val knife = new Weapon(920, 2, "knife")
  val pitchfork = new Weapon(2300, 5, "pitchfork")

  val pawn1: Pawn = Pawn("Mate", 40, List(knife))
  val pawn2: Pawn = Pawn("John", 40, List(pitchfork))

  "battle" should {
    "exit with list of actions if one of the opponents has 0 hp" in {
      val actions = Battle(pawn1, Pawn("test", 0, List.empty)).calculate()
      actions.length shouldEqual 0
    }

    "exit with at least one action if both of opponents has more than 0 hp" in {
      val actions = Battle(pawn1, pawn2).calculate()
      actions.length should be > 0
    }
  }
}
