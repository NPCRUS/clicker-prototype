package models

import game.items.Constants
import utils.AppConfig
import utils.MyPostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Transactions
  extends AppConfig {
  def createUser(token: Token): Future[User] = {
    db.run(
      (for {
        id <- UserModel.createFromToken(token)
        character <- CharacterModel.create(id)
        user <- UserModel.filter(_.id === id).result.head
        item <- InventoryModel.insertQuery(DbItem.fromGameItem(Constants.defaultItem, user))
        _ <- CharacterModel.upsert(character.copy(mainHand = Some(item.id)))
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
        find(character.greaves),
        find(character.amulet),
        find(character.mainHand),
        find(character.offHand)
      )
    }
  }
}
