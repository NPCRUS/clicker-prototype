package components.me

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.JsonSupport._
import utils.Authenticator

class MeRoute(meController: MeController)(implicit auth: Authenticator) {

  def me: Route = auth.jwtAuthWithUser { user =>
    get {
      complete(user)
    }
  }

  def meCharacter: Route = auth.jwtAuthWithUser { user =>
    get {
      onSuccess(meController.getEquippedItems(user)) { characterResponse =>
        complete(characterResponse)
      }
    }
  }
}
