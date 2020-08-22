package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import config.AppConfig
import config.AppConfig.db
import models.JsonSupport._
import models.{ArmorSetResponse, CharacterModel, CharacterResponse, DbItem, HandleResponse, InventoryModel, Token, Transactions, UserModel}
import util.AppExceptions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class MeRoute {
  def getRoutes: Route = Authenticate.customAuthorization{ token =>
    path("me") {
      get {
        onComplete(
          AppConfig.db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
            case Some(u) => Future(u)
            case None => Transactions.createUser(token)
          }.map { u =>
            UserModel.toUser(u)
          }
        ) {
          case Success(result) =>
            complete(result)
          case Failure(exception) => throw exception
        }
      }
    } ~
    path("me" / "character") {
      onComplete(_getEquippedItems(token)) {
        case Success(result) => complete(result)
        case Failure(exception) => throw exception
      }
    }
  }

  private def _getEquippedItems(token: Token): Future[CharacterResponse] =
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
      case Some(u) => Future(UserModel.toUser(u))
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      Transactions.getCharacterWithDbItems(user)
    }.map { c =>
      CharacterResponse(
        ArmorSetResponse(
          helmet = c.helmet,
          body = c.body,
          gloves = c.gloves,
          boots = c.boots,
          belt = c.belt,
          amulet = c.amulet,
          ring1 = c.ring1,
          ring2 = c.ring2
        ),
        HandleResponse(
          mainHand = c.mainHand,
          offHand = c.offHand
        )
      )
    }
}
