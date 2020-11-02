package components.inventory

import game.JsonSupport.ItemFormat
import game.items._
import models.EquipmentPart._
import models.JsonSupport._
import models._
import spray.json._
import utils.{AppConfig, AppExceptions}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InventoryController
  extends AppConfig {

  def equipItem(user: User, equipItemRequest: EquipItemRequest): Future[Int] = {
    (for {
      item <- db.run(InventoryModel.getItemByIdAndUser(equipItemRequest.itemId, user))
      character <- db.run(CharacterModel.getCharacter(user))
      equippedItems <- db.run(InventoryModel.getItemsByIds(character.getIds))
    } yield (item, character, equippedItems)) flatMap {
      case (None, _, _) => throw new AppExceptions.ItemNotFound
      case (Some(item), character, equippedItems) =>
        if (item.user_id != character.userId) throw new AppExceptions.ItemNotFound
        if (character.getIds.contains(item.id)) throw new AppExceptions.ItemIsAlreadyEquipped
        else {
          val newCharacter = getCharacterWithNewItem(item, character, equipItemRequest.equipmentPart, equippedItems)
          db.run(CharacterModel.upsert(newCharacter))
        }
    }
  }

  private[inventory] def getCharacterWithNewItem(item: DbItem, character: DbCharacter, equipmentPart: EquipmentPart.Type, equippedItems: Seq[DbItem]): DbCharacter = {
    val gameItem = item.toJson.convertTo[Item]
    gameItem match {
      case _: Shield if equipmentPart == OffHand =>
        if (equippedItems.exists(el => el.twoHanded.isDefined && el.twoHanded.get))
          character.equipMainHand(None)
            .equipOffHand(Some(item.id))
        else
          character.equipOffHand(Some(item.id))
      case w: Weapon if w.twoHanded && equipmentPart == MainHand =>
        character.equipOffHand(None)
          .equipMainHand(Some(item.id))
      case w: Weapon if !w.twoHanded && (equipmentPart == MainHand || equipmentPart == OffHand) =>
        if (equippedItems.exists(el => el.twoHanded.isDefined && el.twoHanded.get))
          character.equipMainHand(None)
            .equipWeapon(Some(item.id), equipmentPart)
        else
          character.equipWeapon(Some(item.id), equipmentPart)
      case a: Armor if a.armorPart.toString == equipmentPart.toString =>
        character.equipArmor(Some(item.id), equipmentPart)
      case _ => throw new AppExceptions.EquipPartAndItemNotCompatible
    }
  }

  def unequipItem(user: User, unequipItemRequest: UnequipItemRequest): Future[Int] = {
    db.run(CharacterModel.getCharacter(user)) flatMap { character =>
      val newCharacter = unequipItemRequest.equipmentPart match {
        case MainHand => character.equipMainHand(None)
        case OffHand => character.equipOffHand(None)
        case _ => character.equipArmor(None, unequipItemRequest.equipmentPart)
      }

      db.run(CharacterModel.upsert(newCharacter))
    }
  }

  def createItem(user: User, item: Item): Future[DbItem] = {
    def armorToDbItem(a: Armor, u: User): DbItem =
      DbItem(0, a.name, a.cd, a._type.toString, a.passiveEffects, a.activeEffects, u.id, Some(a.armor), Some(a.armorPart.toString), Some(a.armorType.toString), None, None, None, None, a.rarity.toString)

    def weaponToDbItem(w: Weapon, u: User): DbItem =
      DbItem(0, w.name, w.cd, w._type.toString, w.passiveEffects, w.activeEffects, u.id, None, None, None, Some(w.weaponType.toString), Some(w.baseDamage), Some(w.twoHanded), Some(w.damageType.toString), w.rarity.toString)

    val dbItem = item match {
      case a: Armor => armorToDbItem(a, user)
      case w: Weapon => weaponToDbItem(w, user)
    }
    InventoryModel.insert(dbItem)
  }

  def getInventory(user: User): Future[List[DbItem]] = {
    db.run(InventoryModel.getUserInventory(user)).map(_.toList)
  }
}
