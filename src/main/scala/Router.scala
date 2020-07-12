import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object Router {

  def apply(): Route = {
    concat(
      new TestRoute().getRoutes,
      new MeRoute().getRoutes,
      new BattleRoute().getRoutes
    )
  }
}

