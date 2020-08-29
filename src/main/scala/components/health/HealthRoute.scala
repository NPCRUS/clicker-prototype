package components.health

import akka.http.scaladsl.server.Directives.{complete, get}
import akka.http.scaladsl.server.Route

class HealthRoute {
  def health: Route =
    get {
      complete("everything is all right, this is test route")
    }
}
