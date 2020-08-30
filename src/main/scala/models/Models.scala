package models

import game.items._
import spray.json._
import game.JsonSupport._
import JsonSupport._

case class BattlePost(
  mapLevel: Int
)

case class ArmorSetResponse(
  helmet: Option[DbItem],
  body: Option[DbItem],
  gloves: Option[DbItem],
  boots: Option[DbItem],
  belt: Option[DbItem],
  amulet: Option[DbItem],
  ring1: Option[DbItem],
  ring2: Option[DbItem],
)

case class HandleResponse(
  mainHand: Option[DbItem],
  offHand: Option[DbItem]
)

case class CharacterResponse(
  armorSet: ArmorSetResponse,
  handle: HandleResponse
)

object EquipmentPart extends  Enumeration {
  type Type = Value
  val Helmet, Body, Gloves, Boots, Belt, Amulet, Ring1, Ring2, MainHand, OffHand = Value
}

case class EquipItemRequest(
  equipmentPart: EquipmentPart.Type,
  itemId: Int
)

case class UnequipItemRequest(
  equipmentPart: EquipmentPart.Type
)

case class DbCharacterWithDbItems(
  id: Int,
  userId: Int,
  helmet: Option[DbItem],
  body: Option[DbItem],
  gloves: Option[DbItem],
  boots: Option[DbItem],
  belt: Option[DbItem],
  amulet: Option[DbItem],
  ring1: Option[DbItem],
  ring2: Option[DbItem],
  mainHand: Option[DbItem],
  offHand: Option[DbItem]
) {
  def toDbCharacterWithItems: DbCharacterWithItems = {
    DbCharacterWithItems(
      id,
      userId,
      ArmorSet(
        helmet.map(_.toJson.convertTo[Helmet]),
        body.map(_.toJson.convertTo[Body]),
        gloves.map(_.toJson.convertTo[Gloves]),
        boots.map(_.toJson.convertTo[Boots]),
        belt.map(_.toJson.convertTo[Belt]),
        amulet.map(_.toJson.convertTo[Amulet]),
        ring1.map(_.toJson.convertTo[Ring]),
        ring2.map(_.toJson.convertTo[Ring])
      ),
      Handle(
        mainHand.map(_.toJson.convertTo[Weapon]),
        offHand.map(_.toJson.convertTo[Item])
      )
    )
  }
}

case class DbCharacterWithItems(
  id: Int,
  userId: Int,
  armorSet: ArmorSet,
  handle: Handle
)
