import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, ExceptionHandler, RejectionHandler, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import components.battle.{BattleController, BattleRoute}
import components.health.HealthRoute
import components.inventory.{InventoryController, InventoryRoute}
import components.me.{MeController, MeRoute}
import util.AppExceptions

object Router {

  val exceptionHandler: ExceptionHandler = ExceptionHandler(AppExceptions(_))

  val handleErrors: Directive0 = handleRejections(corsRejectionHandler.withFallback(RejectionHandler.default)) &
    handleExceptions(exceptionHandler)

  def middlewares(r: Route): Route = {
    cors() {
      handleErrors {
        r
      }
    }
  }

  // controllers
  val meController = new MeController
  val inventoryController = new InventoryController
  val battleController = new BattleController

  // routes
  val healthRoute = new HealthRoute
  val meRoute = new MeRoute(meController)
  val inventoryRoute = new InventoryRoute(inventoryController)
  val battleRoute = new BattleRoute(battleController)

  def apply(): Route = middlewares {
    concat(
      // health
      path("health")(healthRoute.health),

      // me
      path("me")(meRoute.me),
      path("me" / "character")(meRoute.meCharacter),

      // inventory
      path("inventory")(inventoryRoute.inventory),
      path("inventory" / "equip")(inventoryRoute.equip),
      path("inventory" / "unequip")(inventoryRoute.unequip),

      // battle routes
      path("battle")(battleRoute.battle)
    )
  }
}

