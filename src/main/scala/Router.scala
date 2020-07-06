import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import routes.TestRoute

object Router {

  def apply(): Route = {
    concat(
      new TestRoute().getRoutes,
    )
  }
}

