import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import game._
import game.JsonSupport._

class BattleRoute {
  def getRoutes: Route = path("battle") {
    post {
      Authenticate.customAuthorization{ token =>
        val armorSet = ArmorSet(Some(Helmet("great helmet", 1, 10, List.empty)), None, None, None, None, None, None, None)
        val userPawn = new Pawn(token.opaque_user_id, OneHandedHandle(Dagger("fist", 1000, 0), None), armorSet, InitialProperties())

        val pitchfork = Polearm("pitchfork", 2300, 5, twoHanded = true)
        val botPawn = new Pawn("bot", TwoHandedHandle(pitchfork), ArmorSet.empty, InitialProperties())

        val battle = new Battle(userPawn, botPawn)
        val log = battle.calculate()
        complete(log)
      }
    }
  }
}
