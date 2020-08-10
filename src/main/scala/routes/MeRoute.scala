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


  private def _getEquippedItems(token: Token): Future[CharacterResponse] = {
    def getItem(id: Option[Int], items: Seq[DbItem]): Option[DbItem] = {
      if(id.isEmpty) None
      else items.find(_.id == id)
    }

    db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
      case Some(u) => Future(UserModel.toUser(u))
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      for {
        character <- db.run(CharacterModel.getCharacter(user)).map(CharacterModel.toDbCharacter)
        items <- db.run(InventoryModel.getItemsByIds(character.getIds)).map(_.map(InventoryModel.toDbItem))
      } yield {
        CharacterResponse(
          ArmorSetResponse(
            helmet = getItem(character.helmet, items),
            body = getItem(character.body, items),
            gloves = getItem(character.gloves, items),
            boots = getItem(character.boots, items),
            belt = getItem(character.belt, items),
            amulet = getItem(character.amulet, items),
            ring1 = getItem(character.ring1, items),
            ring2 = getItem(character.ring2, items)
          ),
          HandleResponse(
            mainHand = getItem(character.mainHand, items),
            offHand = getItem(character.offHand, items)
          )
        )
      }
    }

  }
}
