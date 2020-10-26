package game

import game.items._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BattleSpec extends AnyWordSpecLike with Matchers {
  val knife: Weapon = Dagger("knife", 920, 2)
  val pitchfork: Weapon = Polearm("pitchfork", 2300, 5, twoHanded = true)

  val pawn1: Pawn = Pawn("Mate", OneHandedHandle(knife, None), ArmorSet.empty, InitialProperties())
  val pawn2: Pawn = Pawn("John", TwoHandedHandle(pitchfork), ArmorSet.empty, InitialProperties())

  "battle" should {
    "exit with list of actions if one of the opponents has 0 hp" in {
      val actions = Battle(pawn1, Pawn("test", OneHandedHandle(Dagger("fist", 1000, 0), None), ArmorSet.empty, InitialProperties(0))).calculate()
      actions.length shouldEqual 0
    }

    "exit with at least one action if both of opponents has more than 0 hp" in {
      val actions = Battle(pawn1, pawn2).calculate()
      actions.length should be > 0
    }
  }
}
