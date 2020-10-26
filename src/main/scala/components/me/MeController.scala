package components.me

import models._
import utils.AppConfig

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
          greaves = c.greaves,
          amulet = c.amulet
        ),
        HandleResponse(
          mainHand = c.mainHand,
          offHand = c.offHand
        )
      )
    }
  }
}
