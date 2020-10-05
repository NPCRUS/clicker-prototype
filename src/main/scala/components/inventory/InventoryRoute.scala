package components.inventory

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import game.items.Item
import models.{EquipItemRequest, UnequipItemRequest}
import utils.Authenticator
import models.JsonSupport._
import game.JsonSupport.ItemFormat

class InventoryRoute(controller: InventoryController)(implicit auth: Authenticator) {
  def inventory: Route = auth.jwtAuthWithUser { user =>
    get {
      onSuccess(controller.getInventory(user)) { result =>
        complete(result)
      }
    } ~
    post {
      entity(as[Item]) { item =>
        onSuccess(controller.createItem(user, item)) { item =>
          complete(item)
        }
      }
    }
  }

  def equip: Route = auth.jwtAuthWithUser { user =>
    entity(as[EquipItemRequest]) { equipItemRequest =>
      onSuccess(controller.equipItem(user, equipItemRequest)) { _ =>
        complete(StatusCodes.OK)
      }
    }
  }

  def unequip: Route = auth.jwtAuthWithUser { user =>
    entity(as[UnequipItemRequest]) { unequipItemRequest =>
      onSuccess(controller.unequipItem(user, unequipItemRequest)) { _ =>
        complete(StatusCodes.OK)
      }
    }
  }
}
