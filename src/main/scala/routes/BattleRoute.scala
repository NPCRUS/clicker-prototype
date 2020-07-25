package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import game.JsonSupport._
import game._
import game.items._

class BattleRoute {
  def getRoutes: Route = path("battle") {
    post {
      Authenticate.customAuthorization{ token =>
        val knife = Dagger(
          "knife",
          cd = 10000,
          20,
          DamageType.Physical,
          List(PassiveEffect(EffectTargetType.ColdRes, 1000)),
          List(PeriodicActiveEffect(ActiveEffectType.Periodic, EffectTargetType.Hp, chance = 1.0, change = 10, self = true, 4, 2000)))
        val handle1 = OneHandedHandle(knife, None)
        val pawn1 = Pawn("Mate", handle1, ArmorSet.empty, InitialProperties())

        val pitchfork = Polearm(
          "pitchfork",
          cd = 7000,
          15,
          twoHanded = true,
          DamageType.Physical, List(PassiveEffect(EffectTargetType.ColdMit, 1000))
        )
        val handle2 = TwoHandedHandle(pitchfork)
        val body = Body("best", 10000, 100, List.empty)
        val armorSet = ArmorSet(None, Some(body), None, None, None, None, None, None)
        val pawn2 = Pawn("John", handle2, armorSet, InitialProperties(100))

        val battle = new Battle(pawn1, pawn2)
        val actions = battle.calculate()
        complete(actions)
      }
    }
  }
}
