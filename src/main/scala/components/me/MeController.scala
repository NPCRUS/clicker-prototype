package components.me

import models.{ArmorSetResponse, CharacterResponse, HandleResponse, Token, Transactions, User, UserModel}
import utils.{AppConfig, AppExceptions}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MeController
  extends AppConfig {
  
  def getEquippedItems(user: User): Future[CharacterResponse] = {
    Transactions.getCharacterWithDbItems(user).map { c =>
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
}
