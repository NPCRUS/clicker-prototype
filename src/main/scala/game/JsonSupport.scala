package game

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import game.ArmorType.ArmorType
import game.HandleType.HandleType
import game.WeaponType.WeaponType
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

  implicit object WeaponTypeFormat extends RootJsonFormat[WeaponType.WeaponType] {
    override def write(obj: WeaponType): JsValue = JsString(obj.toString)

    override def read(json: JsValue): WeaponType = json match {
      case JsString(str) => WeaponType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object HandleTypeFormat extends RootJsonFormat[HandleType.HandleType] {
    override def write(obj: HandleType): JsValue = JsString(obj.toString)

    override def read(json: JsValue): HandleType = json match {
      case JsString(str) => HandleType.withName(str)
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
  implicit val shieldProtocol: RootJsonFormat[Shield] = new ArmorTypedProtocol[Shield](Shield.apply)
  implicit val armorSetProtocol: RootJsonFormat[ArmorSet] = jsonFormat8(ArmorSet.apply)

  implicit object ArmorFormat extends RootJsonFormat[Armor] {
    override def write(obj: Armor): JsValue =
      JsObject((obj match {
        case h: Helmet => h.toJson
        case b: Body => b.toJson
        case g: Gloves => g.toJson
        case boo: Boots => boo.toJson
        case b: Belt => b.toJson
        case a: Amulet => a.toJson
        case r: Ring => r.toJson
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }).asJsObject.fields)

    override def read(json: JsValue): Armor = {
      val armorType = json.asJsObject.getFields("armorType") match {
        case Seq(JsString(m)) => ArmorType.withName(m)
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }

      armorType match {
        case ArmorType.Helmet => json.convertTo[Helmet]
        case ArmorType.Body => json.convertTo[Body]
        case ArmorType.Gloves => json.convertTo[Gloves]
        case ArmorType.Boots => json.convertTo[Boots]
        case ArmorType.Belt => json.convertTo[Belt]
        case ArmorType.Amulet => json.convertTo[Amulet]
        case ArmorType.Ring => json.convertTo[Ring]
      }
    }
  }

  // WEAPON
  class WeaponTypedProtocol[T <: Weapon](
    factory: (String, Int, Int, Boolean) => T
  ) extends RootJsonFormat[T] {
    override def write(obj: T): JsValue = {
      JsObject(
        "name" -> JsString(obj.name),
        "cd" -> JsNumber(obj.cd),
        "baseDamage" -> JsNumber(obj.baseDamage),
        "twoHanded" -> JsBoolean(obj.twoHanded),
        "weaponType" -> obj.weaponType.toJson,
        "_type" -> JsString(obj._type),
      )
    }

    override def read(json: JsValue): T = {
      val fields = json.asJsObject.fields
      factory(
        fields("name").convertTo[String],
        fields("cd").convertTo[Int],
        fields("baseDamage").convertTo[Int],
        fields("twoHanded").convertTo[Boolean]
      )
    }
  }
  implicit val swordProtocol: RootJsonFormat[Sword] = new WeaponTypedProtocol[Sword](Sword.apply)
  implicit val spearProtocol: RootJsonFormat[Polearm] = new WeaponTypedProtocol[Polearm](Polearm.apply)
  implicit object daggerProtocol extends WeaponTypedProtocol[Dagger](
    (str, int1, int2, bool) => Dagger(str, int1, int2)
  ) {
    override def read(json: JsValue): Dagger = {
      val fields = json.asJsObject.fields
      Dagger.apply(
        fields("name").convertTo[String],
        fields("cd").convertTo[Int],
        fields("baseDamage").convertTo[Int]
      )
    }
  }

  implicit object WeaponFormat extends RootJsonFormat[Weapon] {
    override def write(obj: Weapon): JsValue =
      JsObject((obj match {
        case s: Sword => s.toJson
        case d: Dagger => d.toJson
        case p: Polearm => p.toJson
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }).asJsObject.fields)

    override def read(json: JsValue): Weapon = {
      val fields = json.asJsObject.fields
      val weaponType = fields.get("weaponType") match {
        case Some(JsString(s)) => WeaponType.withName(s)
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }

      weaponType match {
        case WeaponType.Sword => json.convertTo[Sword]
        case WeaponType.Dagger => json.convertTo[Dagger]
        case WeaponType.Polearm => json.convertTo[Polearm]
      }
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

  // HANDLES
  implicit object dualHandleFormat extends RootJsonFormat[DualHandle] {
    override def write(obj: DualHandle): JsValue = {
      JsObject(
        "mainHand" -> obj.mainHand.toJson,
        "offHand" -> obj.offHand.toJson,
        "_type" -> obj._type.toJson
      )
    }

    override def read(json: JsValue): DualHandle = {
      val fields = json.asJsObject.fields
      DualHandle(
        fields("mainHand").convertTo[Weapon],
        fields("offHand").convertTo[Weapon]
      )
    }
  }

  implicit object TwoHandedHandleFormat extends RootJsonFormat[TwoHandedHandle] {
    override def write(obj: TwoHandedHandle): JsValue = {
      JsObject(
        "mainHand" -> obj.mainHand.toJson,
        "_type" -> obj.mainHand._type.toJson
      )
    }

    override def read(json: JsValue): TwoHandedHandle = {
      val fields = json.asJsObject.fields
      TwoHandedHandle(
        fields("mainHand").convertTo[Weapon]
      )
    }
  }

  implicit object oneHandedHandleFormat extends RootJsonFormat[OneHandedHandle] {
    override def write(obj: OneHandedHandle): JsValue = {
      JsObject(
        "mainHand" -> obj.mainHand.toJson,
        "offHand" -> obj.offHand.toJson,
        "_type" -> obj._type.toJson
      )
    }

    override def read(json: JsValue): OneHandedHandle = {
      val fields = json.asJsObject.fields
      OneHandedHandle(
        fields("mainHand").convertTo[Weapon],
        fields("offHand").convertTo[Option[Shield]]
      )
    }
  }

  implicit object HandleFormat extends RootJsonFormat[Handle] {
    override def write(obj: Handle): JsValue =
      JsObject((obj match {
        case d: DualHandle => d.toJson
        case t: TwoHandedHandle => t.toJson
        case o: OneHandedHandle => o.toJson
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }).asJsObject.fields)

    override def read(json: JsValue): Handle = {
      val fields = json.asJsObject.fields
      val handleType = fields.get("_type") match {
        case Some(JsString(s)) => HandleType.withName(s)
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }

      handleType match {
        case HandleType.DualHand => json.convertTo[DualHandle]
        case HandleType.OneHand => json.convertTo[OneHandedHandle]
        case HandleType.TwoHand => json.convertTo[TwoHandedHandle]
      }
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
