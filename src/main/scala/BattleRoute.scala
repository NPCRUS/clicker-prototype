import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import game.{ArmorSet, Battle, Helmet, Pawn, Weapon}

// import JsonSupport._
import game.JsonSupport._

class BattleRoute {
  def getRoutes: Route = path("battle") {
    post {
      Authenticate.customAuthorization{ token =>
        var armorSet = ArmorSet(Some(Helmet("great helmet", 1, 10)), None, None, None, None, None, None, None)
        val userPawn = new Pawn(token.opaque_user_id, 100, List.empty, armorSet)

        val pitchfork = Weapon("pitchfork", 2300,5, twoHanded = true)
        val botPawn = new Pawn("bot", 100, List(pitchfork), ArmorSet.empty)

        val battle = new Battle(userPawn, botPawn)
        val log = battle.calculate()
        complete(log)
      }
    }
  }
}
