package components.battle

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.BattlePost
import models.JsonSupport._
import utils.Authenticator

class BattleRoute(controller: BattleController)(implicit auth: Authenticator) {

  import game.JsonSupport._

  def battle: Route = auth.jwtAuthWithUser { user =>
    post {
      entity(as[BattlePost])(battlePost =>
        onSuccess(controller.battle(user, battlePost)) { result =>
          complete(result)
        }
      )
    }
  }
}
