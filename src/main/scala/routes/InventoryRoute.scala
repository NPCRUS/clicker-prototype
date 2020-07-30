package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import config.AppConfig
import models._
import models.JsonSupport._
import util.AppExceptions

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class InventoryRoute {
  def getRoutes: Route = path("inventory") {
    Authenticate.customAuthorization { token =>
      concat {
        get {
          onComplete(inventory(token)) {
            case Success(result) =>
              complete(result)
            case Failure(exception) => throw exception
          }
        }
      }
    }
  }

  private def inventory(token: Token): Future[List[DbItem]] = {
    AppConfig.db.run(Schemas.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      AppConfig.db.run(Schemas.getUserInventory(user))
    }.map { items =>
      items.toList.map { i =>
        DbItem(i.id, i.name, i.cd, i._type, i.passiveEffects, i.activeEffects, i.user_id, i.armor, i.armorType, i.weaponType, i.baseDamage, i.twoHanded, i.damageType)
      }
    }
  }
}
