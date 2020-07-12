package game

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  // ITEMS
  implicit val weaponProtocol: RootJsonFormat[Weapon] = jsonFormat5(Weapon)
  implicit val armorProtocol: RootJsonFormat[Armor] = jsonFormat4(Armor)
  implicit val armorSetProtocol: RootJsonFormat[ArmorSet] = jsonFormat8(ArmorSet.apply)

  implicit object ItemFormat extends RootJsonFormat[Item] {
    override def write(obj: Item): JsValue =
      JsObject((obj match {
        case w: Weapon => w.toJson
        case a: Armor => a.toJson
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }).asJsObject.fields)

    override def read(json: JsValue): Item =
      json.asJsObject.getFields("_type") match {
        case Seq(JsString("weapon")) => json.convertTo[Weapon]
        case Seq(JsString("armor")) => json.convertTo[Armor]
      }
  }

  // PAWNS/OPPONENTS
  implicit val pawnProtocol: RootJsonFormat[Pawn] = jsonFormat4(Pawn.apply)
  implicit object OpponentFormat extends RootJsonFormat[Opponent] {
    override def write(obj: Opponent): JsValue = {
      JsObject(
        "pawn" ->  obj.pawn.toJson,
        "hp" -> JsNumber(obj.hp),
        "armorSum" -> JsNumber(obj.armorSum)
      )
    }

    override def read(json: JsValue): Opponent = {
      val fields = json.asJsObject.fields
      Opponent(
        fields("pawn").convertTo[Pawn],
        fields("hp").convertTo[Int]
      )
    }
  }

  // ACTIONS
  implicit val attackProtocol: RootJsonFormat[Attack] = jsonFormat6(Attack.apply)

  implicit object ActionFormat extends RootJsonFormat[Action] {
    override def write(obj: Action): JsValue =
      JsObject((obj match {
        case a: Attack => a.toJson
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }).asJsObject.fields)

    override def read(json: JsValue): Action =
      json.asJsObject.getFields("_type") match {
        case Seq(JsString("attack")) => json.convertTo[Attack]
      }
  }
}
