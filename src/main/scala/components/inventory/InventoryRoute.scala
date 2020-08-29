package components.inventory

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import game.items.Item
import models.{EquipItemRequest, UnequipItemRequest}
import util.Authenticate
import models.JsonSupport._
import game.JsonSupport.ItemFormat

import scala.util.{Failure, Success}

class InventoryRoute(controller: InventoryController) {
  def inventory: Route = Authenticate.customAuthorization { token =>
    get {
      onComplete(controller.getInventory(token)) {
        case Success(result) => complete(result)
        case Failure(exception) => throw exception
      }
    } ~
    post {
      entity(as[Item]) { item =>
        onComplete(controller.createItem(token, item)) {
          case Success(item) => complete(item)
          case Failure(exception) => throw exception
        }
      }
    }
  }

  def equip: Route = Authenticate.customAuthorization { token =>
    entity(as[EquipItemRequest]) { equipItemRequest =>
      onComplete(controller.equipItem(token, equipItemRequest)) {
        case Success(value) => complete(StatusCodes.OK)
        case Failure(exception) =>
          throw exception
      }
    }
  }

  def unequip: Route = Authenticate.customAuthorization { token =>
    entity(as[UnequipItemRequest]) { unequipItemRequest =>
      onComplete(controller.unequipItem(token, unequipItemRequest)) {
        case Success(_) => complete(StatusCodes.OK)
      }
    }
  }
}
