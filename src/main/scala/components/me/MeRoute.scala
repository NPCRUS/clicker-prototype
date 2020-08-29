package components.me

import akka.http.scaladsl.server.Directives.{complete, get, onComplete}
import akka.http.scaladsl.server.Route
import models.JsonSupport._
import util.Authenticate

import scala.util.Success

class MeRoute(meController: MeController) {

  def me: Route = Authenticate.customAuthorization { token =>
      get {
        onComplete(meController.me(token)) {
          case Success(result) => complete(result)
        }
      }
  }

  def meCharacter: Route = Authenticate.customAuthorization { token =>
    onComplete(meController.getEquippedItems(token)) {
      case Success(result) => complete(result)
    }
  }
}
