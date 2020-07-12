import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

class TestRoute {
  def getRoutes: Route = path("health") {
    concat {
      cors() {
        get {
          complete("everything is all right, this is test route")
        }
      }
    }
  }

}
