package components.battle

import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import models.BattlePost
import util.Authenticate

import scala.util.Success
import models.JsonSupport._

class BattleRoute(controller: BattleController) {
  import game.JsonSupport._

  def battle: Route = Authenticate.customAuthorization { token =>
    post {
      entity(as[BattlePost])(battlePost =>
        onComplete(controller.battle(token, battlePost)) {
          case Success(result) => complete(result)
        }
      )
    }
  }
}
