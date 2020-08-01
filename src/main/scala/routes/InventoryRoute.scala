package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import config.AppConfig
import game.items.Item
import models._
import models.JsonSupport._
import util.AppExceptions
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class InventoryRoute {
  import game.JsonSupport._

  def getRoutes: Route = Authenticate.customAuthorization { token =>
    path("inventory") {
      get {
        onComplete(getInventory(token)) {
          case Success(result) =>
            complete(result)
          case Failure(exception) => throw exception
        }
      }
    }
  }

  private def createItem(token: Token, item: Item): Future[Int] = {
    ???
  }

  private def getInventory(token: Token): Future[List[DbItem]] = {
    AppConfig.db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      AppConfig.db.run(InventoryModel.getUserInventory(user))
    }.map { items =>
      items.toList.map(i => InventoryModel.toDbItem(i))
    }
  }
}
