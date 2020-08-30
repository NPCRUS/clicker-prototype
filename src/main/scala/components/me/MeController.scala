package components.me

import models.{ArmorSetResponse, CharacterResponse, HandleResponse, Token, Transactions, User, UserModel}
import util.{AppConfig, AppExceptions}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MeController
  extends AppConfig {

  def me(token: Token): Future[User] = {
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
      case Some(u) => Future(u)
      case None => Transactions.createUser(token)
    }
  }

  def getEquippedItems(token: Token): Future[CharacterResponse] =
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
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
