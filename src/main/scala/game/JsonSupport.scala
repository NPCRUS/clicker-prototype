package game

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import game.ArmorType.ArmorType
import spray.json._

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  // ITEMS
  implicit object ArmorTypeFormat extends RootJsonFormat[ArmorType.ArmorType] {
    override def write(obj: ArmorType): JsValue = JsString(obj.toString)

    override def read(json: JsValue): ArmorType = json match {
      case JsString(str) => ArmorType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  // ARMOR
  class ArmorTypedProtocol[T <: Armor](
    factory: (String, Int, Int) => T
  ) extends RootJsonFormat[T] {
    override def write(obj: T): JsValue = {
      JsObject(
        "name" -> JsString(obj.name),
        "cd" -> JsNumber(obj.cd),
        "armor" -> JsNumber(obj.armor),
        "armorType" -> obj.armorType.toJson,
        "_type" -> JsString(obj._type),
      )
    }

    override def read(json: JsValue): T = {
      val fields = json.asJsObject.fields
      factory(
        fields("name").convertTo[String],
        fields("cd").convertTo[Int],
        fields("armor").convertTo[Int]
      )
    }
  }
  implicit val helmetProtocol: RootJsonFormat[Helmet] = new ArmorTypedProtocol[Helmet](Helmet.apply)
  implicit val bodyProtocol: RootJsonFormat[Body] = new ArmorTypedProtocol[Body](Body.apply)
  implicit val glovesProtocol: RootJsonFormat[Gloves] = new ArmorTypedProtocol[Gloves](Gloves.apply)
  implicit val bootsProtocol: RootJsonFormat[Boots] = new ArmorTypedProtocol[Boots](Boots.apply)
  implicit val beltProtocol: RootJsonFormat[Belt] = new ArmorTypedProtocol[Belt](Belt.apply)
  implicit val amuletProtocol: RootJsonFormat[Amulet] = new ArmorTypedProtocol[Amulet](Amulet.apply)
  implicit val ringProtocol: RootJsonFormat[Ring] = new ArmorTypedProtocol[Ring](Ring.apply)
  implicit val armorSetProtocol: RootJsonFormat[ArmorSet] = jsonFormat8(ArmorSet.apply)

  implicit object ArmorFormat extends RootJsonFormat[Armor] {
    override def write(obj: Armor): JsValue =
      JsObject((obj match {
        case h: Helmet => h.toJson
        case b: Body => b.toJson
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }).asJsObject.fields)

    override def read(json: JsValue): Armor =
      json.asJsObject.getFields("armorType") match {
        case Seq(JsString("helmet")) => json.convertTo[Helmet]
        case Seq(JsString("body")) => json.convertTo[Body]
      }
  }

  // WEAPON
  implicit object WeaponFormat extends RootJsonFormat[Weapon] {
    override def write(obj: Weapon): JsValue = {
      JsObject(
        "name" -> JsString(obj.name),
        "cd" -> JsNumber(obj.cd),
        "baseDamage" -> JsNumber(obj.baseDamage),
        "twoHanded" -> JsBoolean(obj.twoHanded),
        "_type" -> JsString(obj._type),
      )
    }

    override def read(json: JsValue): Weapon = {
      val fields = json.asJsObject.fields
      Weapon(
        fields("name").convertTo[String],
        fields("cd").convertTo[Int],
        fields("baseDamage").convertTo[Int],
        fields("twoHanded").convertTo[Boolean]
      )
    }
  }

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
