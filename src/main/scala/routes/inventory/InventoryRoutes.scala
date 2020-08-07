package routes.inventory

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import routes.Authenticate
import InventoryController._

object InventoryRoutes {
  def apply(): Route = {
    Authenticate.customAuthorization { token =>
      concat(
        path("inventory")(get(getInventory(token))),
        path("inventory" / "equipped")(get(getEquippedItems(token))),
        path("inventory")(post(createItem(token))),
        path("inventory" / "equip")(post(equipItem(token)))
      )
    }
  }
}
