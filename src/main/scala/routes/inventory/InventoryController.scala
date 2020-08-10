package routes.inventory

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import config.AppConfig._
import game.items.{Armor, ArmorType, Item, Shield, Weapon}
import models.{CharacterModel, DbCharacter, DbItem, EquipItemRequest, EquipmentPart, InventoryModel, Token, User, UserModel}
import spray.json._
import models.JsonSupport._
import util.AppExceptions
import EquipmentPart._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object InventoryController {
  import game.JsonSupport._

  def getInventory(token: Token) = {
    onComplete(_getInventory(token)) {
      case Success(result) => complete(result)
      case Failure(exception) => throw exception
    }
  }

  def createItem(token: Token) = {
    entity(as[Item]) { item =>
      onComplete(_createItem(token, item)) {
        case Success(item) => complete(item)
        case Failure(exception) => throw exception
      }
    }
  }

  def equipItem(token: Token) = {
    entity(as[EquipItemRequest]) { equipItemRequest =>
      onComplete(_equipItem(token, equipItemRequest)) {
        case Success(value) => complete(StatusCodes.OK)
        case Failure(exception) =>
          throw exception
      }
    }
  }

  private def _equipItem(token: Token, equipItemRequest: EquipItemRequest): Future[Int] = {
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
      case Some(u) => Future(UserModel.toUser(u))
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      for {
        item <- db.run(InventoryModel.getItemByIdAndUser(equipItemRequest.itemId, user))
        character <- db.run(CharacterModel.getCharacter(user)).map(CharacterModel.toDbCharacter)
        equippedItems <- db.run(InventoryModel.getItemsByIds(character.getIds)).map(_.map(InventoryModel.toDbItem))
      } yield (item, character, equippedItems)
    }.flatMap {
      case (None, _, _) => throw new AppExceptions.ItemNotFound
      case (Some(item), character, equippedItems) =>
        if(item.user_id != character.userId) throw new AppExceptions.ItemNotFound
        if(character.getIds.contains(item.id.get)) throw new AppExceptions.ItemIsAlreadyEquipped
        else {
          val dbItem = InventoryModel.toDbItem(item)
          val newCharacter = getCharacterWithNewItem(dbItem, character, equipItemRequest.equipmentPart, equippedItems)

          db.run(CharacterModel.upsert(newCharacter))
        }
    }
  }

  private def _createItem(token: Token, item: Item): Future[DbItem] = {
    def armorToDbItem(a: Armor, u: User): DbItem =
      DbItem(None, a.name, a.cd, a._type.toString, a.passiveEffects, a.activeEffects, u.id.get, Some(a.armor), Some(a.armorType.toString), None, None, None, None)

    def weaponToDbItem(w: Weapon, u: User): DbItem =
      DbItem(None, w.name, w.cd, w._type.toString, w.passiveEffects, w.activeEffects, u.id.head, None, None, Some(w.weaponType.toString), Some(w.baseDamage), Some(w.twoHanded), Some(w.damageType.toString))

    db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      val dbItem = item match {
        case a: Armor => armorToDbItem(a, user)
        case w: Weapon => weaponToDbItem(w, user)
      }
      InventoryModel.insert(dbItem)
    }
  }

  private def _getInventory(token: Token): Future[List[DbItem]] = {
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      db.run(InventoryModel.getUserInventory(user))
    }.map { items =>
      items.toList.map(i => InventoryModel.toDbItem(i))
    }
  }

  def getCharacterWithNewItem(item: DbItem, character: DbCharacter, equipmentPart: EquipmentPart.Type, equippedItems: Seq[DbItem]) = {
    val gameItem = item.toJson.convertTo[Item]
    gameItem match {
      case _: Shield if equipmentPart == OffHand =>
        if(equippedItems.exists(el => el.twoHanded.isDefined && el.twoHanded.get))
          character.equipMainHand(None)
            .equipOffHand(item.id)
        else
          character.equipOffHand(item.id)
      case w: Weapon if w.twoHanded && equipmentPart == MainHand =>
        character.equipOffHand(None)
          .equipMainHand(item.id)
      case w: Weapon if !w.twoHanded && (equipmentPart == MainHand || equipmentPart == OffHand) =>
        if(equippedItems.exists(el => el.twoHanded.isDefined && el.twoHanded.get))
          character.equipMainHand(None)
            .equipWeapon(item.id, equipmentPart)
        else
          character.equipWeapon(item.id, equipmentPart)
      case a: Armor if a.armorType == ArmorType.Ring && (equipmentPart == Ring1 || equipmentPart == Ring2) =>
        character.equipArmor(item.id.get, equipmentPart)
      case a: Armor if a.armorType.toString == equipmentPart.toString =>
        character.equipArmor(item.id.get, equipmentPart)
      case _ => throw new AppExceptions.EquipPartAndItemNotCompatible
    }
  }
}
