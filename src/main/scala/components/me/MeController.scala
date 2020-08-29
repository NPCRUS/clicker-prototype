package components.me

import config.AppConfig
import config.AppConfig.db
import models.{ArmorSetResponse, CharacterResponse, HandleResponse, Token, Transactions, User, UserModel}
import util.AppExceptions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MeController {

  def me(token: Token): Future[User] = {
    AppConfig.db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
      case Some(u) => Future(u)
      case None => Transactions.createUser(token)
    }.map { u =>
      UserModel.toUser(u)
    }
  }

  def getEquippedItems(token: Token): Future[CharacterResponse] =
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
