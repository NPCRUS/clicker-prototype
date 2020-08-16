package routes.inventory

import game.items._
import models.{DbCharacter, DbItem }
import models.EquipmentPart
import util.AppExceptions._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import InventoryController._

class GetCharacterWithNewItemSpec  extends AnyWordSpecLike with Matchers {
  val helmet: Helmet = Helmet("helmet", 1, 1)
  val body: Body = Body("body", 1, 1)
  val gloves: Gloves = Gloves("gloves", 1, 1)
  val boots: Boots = Boots("boots", 1, 1)
  val belt: Belt = Belt("belt", 1, 1)
  val amulet: Amulet = Amulet("amulet", 1, 1)
  val ring: Ring = Ring("ring", 1, 1)
  val oneHanded: Sword = Sword("sword", 1, 1, twoHanded = false)
  val otherOneHanded: Dagger = Dagger("dagger", 1, 1)
  val twoHanded: Sword = Sword("sword", 1, 1, twoHanded = true)
  val shield: Shield = Shield("shield", 1, 1)
  val emptyCharacter: DbCharacter = DbCharacter(None, 0, None, None, None, None, None, None, None, None, None, None)

  "getCharacterWithNewItem" should {
    "equip helmet into not helmet slot should throw exception" in {
      val dbItem = itemToDbItem(helmet)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip helmet into helmet slot should return new DbCharacter" in {
      val dbItem = itemToDbItem(helmet)
      val equipmentPart = EquipmentPart.Helmet
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipArmor(dbItem.id, equipmentPart))
    }

    "equip body into not body slot should throw exception" in {
      val dbItem = itemToDbItem(body)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip body into body slot should return new DbCharacter" in {
      val dbItem = itemToDbItem(body)
      val equipmentPart = EquipmentPart.Body
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipArmor(dbItem.id, equipmentPart))
    }

    "equip gloves into not gloves slot should throw exception" in {
      val dbItem = itemToDbItem(gloves)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip gloves into gloves slot should return new DbCharacter" in {
      val dbItem = itemToDbItem(gloves)
      val equipmentPart = EquipmentPart.Gloves
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipArmor(dbItem.id, equipmentPart))
    }

    "equip boots into not boots slot should throw exception" in {
      val dbItem = itemToDbItem(boots)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip boots into boots slot should return new DbCharacter" in {
      val dbItem = itemToDbItem(boots)
      val equipmentPart = EquipmentPart.Boots
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipArmor(dbItem.id, equipmentPart))
    }

    "equip belt into not belt slot should throw exception" in {
      val dbItem = itemToDbItem(belt)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip belt into belt slot should return new DbCharacter" in {
      val dbItem = itemToDbItem(belt)
      val equipmentPart = EquipmentPart.Belt
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipArmor(dbItem.id, equipmentPart))
    }

    "equip amulet into not amulet slot should throw exception" in {
      val dbItem = itemToDbItem(amulet)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip amulet into amulet slot should return new DbCharacter" in {
      val dbItem = itemToDbItem(amulet)
      val equipmentPart = EquipmentPart.Amulet
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipArmor(dbItem.id, equipmentPart))
    }

    "equip ring into not ring1 slot should throw exception" in {
      val dbItem = itemToDbItem(ring)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip ring into ring1 slot should return new DbCharacter" in {
      val dbItem = itemToDbItem(ring)
      val equipmentPart = EquipmentPart.Ring1
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipArmor(dbItem.id, equipmentPart))
    }

    "equip ring into not ring2 slot should throw exception" in {
      val dbItem = itemToDbItem(ring)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip ring into ring2 slot should return new DbCharacter" in {
      val dbItem = itemToDbItem(ring)
      val equipmentPart = EquipmentPart.Ring2
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipArmor(dbItem.id, equipmentPart))
    }

    "equip twoHanded weapon not in mainHand slot throw exception" in {
      val dbItem = itemToDbItem(twoHanded)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.OffHand, Seq.empty)
    }

    "equip twoHanded weapon unequips offHand" in {
      val dbItem = itemToDbItem(twoHanded)
      val equipmentPart = EquipmentPart.MainHand
      val oneHandedItem = itemToDbItem(oneHanded)
      val character = emptyCharacter.equipOffHand(oneHandedItem.id)
      val newCharacter = getCharacterWithNewItem(dbItem, character, equipmentPart, Seq(oneHandedItem))

      newCharacter should be (
        character
          .equipOffHand(None)
          .equipMainHand(Some(dbItem.id.get))
      )
    }

    "equip oneHanded not in hands throw exception" in {
      val dbItem = itemToDbItem(oneHanded)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.Helmet, Seq.empty)
    }

    "equip oneHanded into mainHand return new DbCharacter" in {
      val dbItem = itemToDbItem(oneHanded)
      val equipmentPart = EquipmentPart.MainHand
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipMainHand(dbItem.id))
    }

    "equip oneHanded into offHand return new DbCharacter" in {
      val dbItem = itemToDbItem(oneHanded)
      val equipmentPart = EquipmentPart.OffHand
      val newCharacter = getCharacterWithNewItem(dbItem, emptyCharacter, equipmentPart, Seq.empty)

      newCharacter should be (emptyCharacter.equipOffHand(dbItem.id))
    }

    "equip oneHanded into mainHand unequips twoHanded weapon" in {
      val dbItem = itemToDbItem(oneHanded)
      val twoHandedItem = itemToDbItem(twoHanded)
      val equipmentPart = EquipmentPart.MainHand
      val character = emptyCharacter.equipMainHand(twoHandedItem.id)
      val newCharacter = getCharacterWithNewItem(dbItem, character, equipmentPart, Seq(twoHandedItem))

      newCharacter should be (character.equipMainHand(dbItem.id))
    }

    "equip oneHanded into offHand unequips twoHanded weapon" in {
      val dbItem = itemToDbItem(oneHanded)
      val twoHandedItem = itemToDbItem(twoHanded)
      val equipmentPart = EquipmentPart.OffHand
      val character = emptyCharacter.equipMainHand(twoHandedItem.id)
      val newCharacter = getCharacterWithNewItem(dbItem, character, equipmentPart, Seq(twoHandedItem))

      newCharacter should be (
        character
          .equipMainHand(None)
          .equipOffHand(dbItem.id)
      )
    }

    "equip shield not in offHand slot throw exception" in {
      val dbItem = itemToDbItem(shield)

      an [EquipPartAndItemNotCompatible] should be thrownBy
        getCharacterWithNewItem(dbItem, emptyCharacter, EquipmentPart.MainHand, Seq.empty)
    }

    "equip shield in offHand slot unequips twoHanded and return new Character" in {
      val dbItem = itemToDbItem(shield)
      val twoHandedItem = itemToDbItem(twoHanded)
      val equipmentPart = EquipmentPart.OffHand
      val character = emptyCharacter.equipMainHand(twoHandedItem.id)
      val newCharacter = getCharacterWithNewItem(dbItem, character, equipmentPart, Seq(twoHandedItem))

      newCharacter should be(
        character
          .equipMainHand(None)
          .equipOffHand(dbItem.id)
      )
    }
  }

  def itemToDbItem(item: Item): DbItem =
    item match {
      case a: Armor =>
        DbItem(
          Some(1),
          a.name,
          a.cd,
          a._type.toString,
          a.passiveEffects,
          a.activeEffects,
          0,
          Some(a.armor),
          Some(a.armorType.toString),
          None,
          None,
          None,
          None
        )
      case w: Weapon =>
        DbItem(
          Some(1),
          w.name,
          w.cd,
          w._type.toString,
          w.passiveEffects,
          w.activeEffects,
          0,
          None,
          None,
          Some(w.weaponType.toString),
          Some(w.baseDamage),
          Some(w.twoHanded),
          Some(w.damageType.toString)
        )
    }
}
