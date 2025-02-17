package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {

  import game.JsonSupport._

  implicit val PubSubPermsProtocol: RootJsonFormat[PubSubPerms] = jsonFormat2(PubSubPerms)
  implicit val TokenProtocol: RootJsonFormat[Token] = jsonFormat7(Token)

  implicit val UserProtocol: RootJsonFormat[User] = jsonFormat7(User.apply)
  implicit val DbItemProtocol: RootJsonFormat[DbItem] = jsonFormat15(DbItem.apply)
  implicit val battlePostProtocol: RootJsonFormat[BattlePost] = jsonFormat1(BattlePost)

  implicit val handleResponseProtocol: RootJsonFormat[HandleResponse] = jsonFormat2(HandleResponse)
  implicit val armorSetResponseProtocol: RootJsonFormat[ArmorSetResponse] = jsonFormat4(ArmorSetResponse)
  implicit val characterResponseProtocol: RootJsonFormat[CharacterResponse] = jsonFormat2(CharacterResponse)

  implicit object EquipmentPartFormat extends RootJsonFormat[EquipmentPart.Type] {
    override def write(obj: EquipmentPart.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): EquipmentPart.Type = json match {
      case JsString(str) => EquipmentPart.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit val equipItemRequestProtocol: RootJsonFormat[EquipItemRequest] = jsonFormat2(EquipItemRequest)
  implicit val unequipItemRequestProtocol: RootJsonFormat[UnequipItemRequest] = jsonFormat1(UnequipItemRequest)

  implicit val battleResultProtocol: RootJsonFormat[BattleResult] = jsonFormat2(BattleResult)

}
