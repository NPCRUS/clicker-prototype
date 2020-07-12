import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

object Router {

  def apply(): Route = cors() {
    concat(
      new TestRoute().getRoutes,
      new MeRoute().getRoutes,
      new BattleRoute().getRoutes
    )
  }

}

