package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class TestRoute {
  def getRoutes: Route = path("health") {
    concat {
      get {
        complete("everything is all right, this is test route")
      }
    }
  }
}
