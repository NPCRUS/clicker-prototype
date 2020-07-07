import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import routes.{AuthRoute, TestRoute}

object Router {

  def apply(): Route = {
    concat(
      new TestRoute().getRoutes,
      new AuthRoute().getRoutes
    )
  }
}

