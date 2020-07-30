import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, ExceptionHandler, RejectionHandler, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import routes.{BattleRoute, InventoryRoute, MeRoute, TestRoute}
import util.AppExceptions

object Router {

  val exceptionHandler: ExceptionHandler = ExceptionHandler(AppExceptions(_))

  val handleErrors: Directive0 = handleRejections(corsRejectionHandler.withFallback(RejectionHandler.default)) &
    handleExceptions(exceptionHandler)

  def apply(): Route =
    cors() {
      handleErrors {
        new TestRoute().getRoutes ~
          new MeRoute().getRoutes ~
          new BattleRoute().getRoutes ~
          new InventoryRoute().getRoutes
      }
    }

}

