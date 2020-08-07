package models

import game.items.ArmorType

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
