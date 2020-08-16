package routes.inventory

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import routes.Authenticate
import InventoryController._

object InventoryRoutes {
  def apply(): Route = {
    Authenticate.customAuthorization { token =>
      path("inventory")(get(getInventory(token))) ~
      path("inventory")(post(createItem(token))) ~
      path("inventory" / "equip")(post(equipItem(token))) ~
      path("inventory" / "unequip")(post(unequipItem(token)))
    }
  }
}
