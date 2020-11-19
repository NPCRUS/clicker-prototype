package models

import game.Action
import game.JsonSupport._
import game.items._
import models.JsonSupport._
import spray.json._

case class BattlePost(mapLevel: Int)

case class ArmorSetResponse(helmet: Option[DbItem],
                            body: Option[DbItem],
                            greaves: Option[DbItem],
                            amulet: Option[DbItem])

case class HandleResponse(mainHand: Option[DbItem],
                          offHand: Option[DbItem])

case class CharacterResponse(armorSet: ArmorSetResponse,
                             handle: HandleResponse)

object EquipmentPart extends Enumeration {
  type Type = Value
  val Helmet, Body, Greaves, Amulet, MainHand, OffHand = Value
}

case class EquipItemRequest(equipmentPart: EquipmentPart.Type,
                            itemId: Int)

case class UnequipItemRequest(equipmentPart: EquipmentPart.Type)

case class DbCharacterWithDbItems(id: Int,
                                  userId: Int,
                                  helmet: Option[DbItem],
                                  body: Option[DbItem],
                                  greaves: Option[DbItem],
                                  amulet: Option[DbItem],
                                  mainHand: Option[DbItem],
                                  offHand: Option[DbItem]) {
  def toDbCharacterWithItems: DbCharacterWithItems = {
    DbCharacterWithItems(
      id,
      userId,
      ArmorSet(
        helmet.map(_.toJson.convertTo[Helmet]),
        body.map(_.toJson.convertTo[Body]),
        greaves.map(_.toJson.convertTo[Greaves]),
        amulet.map(_.toJson.convertTo[Amulet]),
      ),
      Handle(
        mainHand.map(_.toJson.convertTo[Weapon]),
        offHand.map(_.toJson.convertTo[Item])
      )
    )
  }
}

case class DbCharacterWithItems(id: Int,
                                userId: Int,
                                armorSet: ArmorSet,
                                handle: Handle)

case class BattleResult(actions: List[Action],
                        reward: List[DbItem])
