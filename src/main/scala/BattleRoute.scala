import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import game.{ArmorSet, Battle, Pawn, Weapon}

// import JsonSupport._
import game.JsonSupport._

class BattleRoute {
  def getRoutes: Route = path("battle") {
    post {
      Authenticate.customAuthorization{ token =>
        val userPawn = new Pawn(token.opaque_user_id, 100, List.empty, ArmorSet.empty)

        val pitchfork = Weapon("pitchfork", true, 2300,5)
        val botPawn = new Pawn("bot", 100, List(pitchfork), ArmorSet.empty)

        val battle = new Battle(userPawn, botPawn)
        val log = battle.calculate()
        complete(log)
      }
    }
  }
}
