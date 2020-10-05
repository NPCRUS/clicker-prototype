package models

import utils.AppConfig
import scala.concurrent.Future
import utils.MyPostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

object Transactions
  extends AppConfig {
  def createUser(token: Token): Future[User] = {
    db.run(
      (for {
        id <- UserModel.createFromToken(token)
        _ <- CharacterModel.create(id)
        user <- UserModel.filter(_.id === id).result.head
      } yield user).transactionally
    )
  }

  def getCharacterWithDbItems(user: User): Future[DbCharacterWithDbItems] = {
    for {
      character <- db.run(CharacterModel.getCharacter(user))
      equippedItems <- db.run(InventoryModel.getItemsByIds(character.getIds))
    } yield {
      def find(id: Option[Int]): Option[DbItem] = {
        id.flatMap { id =>
          equippedItems.find(_.id == id)
        }
      }

      DbCharacterWithDbItems(
        character.id,
        character.userId,
        find(character.helmet),
        find(character.body),
        find(character.gloves),
        find(character.boots),
        find(character.belt),
        find(character.amulet),
        find(character.ring1),
        find(character.ring2),
        find(character.mainHand),
        find(character.offHand)
      )
    }
  }
}
