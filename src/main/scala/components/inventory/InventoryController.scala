package components.inventory

import game.items.{Armor, ArmorType, Item, Shield, Weapon}
import models.{CharacterModel, DbCharacter, DbItem, EquipItemRequest, EquipmentPart, InventoryModel, Token, UnequipItemRequest, User, UserModel}
import spray.json._
import models.JsonSupport._
import util.{AppConfig, AppExceptions}
import EquipmentPart._
import game.JsonSupport.ItemFormat

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InventoryController
  extends AppConfig {

  def equipItem(token: Token, equipItemRequest: EquipItemRequest): Future[Int] = {
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      for {
        item <- db.run(InventoryModel.getItemByIdAndUser(equipItemRequest.itemId, user))
        character <- db.run(CharacterModel.getCharacter(user))
        equippedItems <- db.run(InventoryModel.getItemsByIds(character.getIds))
      } yield (item, character, equippedItems)
    }.flatMap {
      case (None, _, _) => throw new AppExceptions.ItemNotFound
      case (Some(item), character, equippedItems) =>
        if(item.user_id != character.userId) throw new AppExceptions.ItemNotFound
        if(character.getIds.contains(item.id)) throw new AppExceptions.ItemIsAlreadyEquipped
        else {
          val newCharacter = getCharacterWithNewItem(item, character, equipItemRequest.equipmentPart, equippedItems)
          db.run(CharacterModel.upsert(newCharacter))
        }
    }
  }

  def unequipItem(token: Token, unequipItemRequest: UnequipItemRequest): Future[Int] = {
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      db.run(CharacterModel.getCharacter(user))
    }.flatMap { character =>
      val newCharacter = unequipItemRequest.equipmentPart match {
        case MainHand => character.equipMainHand(None)
        case OffHand => character.equipOffHand(None)
        case _ => character.equipArmor(None, unequipItemRequest.equipmentPart)
      }

      db.run(CharacterModel.upsert(newCharacter))
    }
  }

  def createItem(token: Token, item: Item): Future[DbItem] = {
    def armorToDbItem(a: Armor, u: User): DbItem =
      DbItem(0, a.name, a.cd, a._type.toString, a.passiveEffects, a.activeEffects, u.id, Some(a.armor), Some(a.armorType.toString), None, None, None, None)

    def weaponToDbItem(w: Weapon, u: User): DbItem =
      DbItem(0, w.name, w.cd, w._type.toString, w.passiveEffects, w.activeEffects, u.id, None, None, Some(w.weaponType.toString), Some(w.baseDamage), Some(w.twoHanded), Some(w.damageType.toString))

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

  def getInventory(token: Token): Future[List[DbItem]] = {
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      db.run(InventoryModel.getUserInventory(user)).map(_.toList)
    }
  }

  def getCharacterWithNewItem(item: DbItem, character: DbCharacter, equipmentPart: EquipmentPart.Type, equippedItems: Seq[DbItem]): DbCharacter = {
    val gameItem = item.toJson.convertTo[Item]
    gameItem match {
      case _: Shield if equipmentPart == OffHand =>
        if(equippedItems.exists(el => el.twoHanded.isDefined && el.twoHanded.get))
          character.equipMainHand(None)
            .equipOffHand(Some(item.id))
        else
          character.equipOffHand(Some(item.id))
      case w: Weapon if w.twoHanded && equipmentPart == MainHand =>
        character.equipOffHand(None)
          .equipMainHand(Some(item.id))
      case w: Weapon if !w.twoHanded && (equipmentPart == MainHand || equipmentPart == OffHand) =>
        if(equippedItems.exists(el => el.twoHanded.isDefined && el.twoHanded.get))
          character.equipMainHand(None)
            .equipWeapon(Some(item.id), equipmentPart)
        else
          character.equipWeapon(Some(item.id), equipmentPart)
      case a: Armor if a.armorType == ArmorType.Ring && (equipmentPart == Ring1 || equipmentPart == Ring2) =>
        character.equipArmor(Some(item.id), equipmentPart)
      case a: Armor if a.armorType.toString == equipmentPart.toString =>
        character.equipArmor(Some(item.id), equipmentPart)
      case _ => throw new AppExceptions.EquipPartAndItemNotCompatible
    }
  }
}
