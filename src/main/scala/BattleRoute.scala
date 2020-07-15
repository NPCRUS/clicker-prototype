import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import game.{Action, ArmorSet, Battle, Halberd, Helmet, Pawn, Weapon}

import spray.json._
// import JsonSupport._
import game.JsonSupport._

class BattleRoute {
  def getRoutes: Route = path("battle") {
    post {
      Authenticate.customAuthorization{ token =>
        val armorSet = ArmorSet(Some(Helmet("great helmet", 1, 10)), None, None, None, None, None, None, None)
        val userPawn = new Pawn(token.opaque_user_id, 100, List.empty, armorSet)

        val pitchfork = Halberd("pitchfork", 2300,5)
        val botPawn = new Pawn("bot", 100, List(pitchfork), ArmorSet.empty)

        val battle = new Battle(userPawn, botPawn)
        val log = battle.calculate()
        complete(log)
      }
    }
  }
}
