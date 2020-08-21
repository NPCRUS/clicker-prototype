package game

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import game.ActionType.Type
import game.items._
import spray.json._

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object ItemTypeFormat extends RootJsonFormat[ItemType.Type] {
    override def write(obj: ItemType.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): ItemType.Type = json match {
      case JsString(str) => ItemType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object ArmorTypeFormat extends RootJsonFormat[ArmorType.Type] {
    override def write(obj: ArmorType.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): ArmorType.Type = json match {
      case JsString(str) => ArmorType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object WeaponTypeFormat extends RootJsonFormat[WeaponType.Type] {
    override def write(obj: WeaponType.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): WeaponType.Type = json match {
      case JsString(str) => WeaponType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object HandleTypeFormat extends RootJsonFormat[HandleType.Type] {
    override def write(obj: HandleType.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): HandleType.Type = json match {
      case JsString(str) => HandleType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object PassiveEffectTypeFormat extends RootJsonFormat[EffectTargetType.Type] {
    override def write(obj: EffectTargetType.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): EffectTargetType.Type = json match {
      case JsString(str) => EffectTargetType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object AvoidanceTypeFormat extends RootJsonFormat[AvoidanceType.Type] {
    override def write(obj: AvoidanceType.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): AvoidanceType.Type = json match {
      case JsString(str) => AvoidanceType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object DamageTypeFormat extends RootJsonFormat[DamageType.Type] {
    override def write(obj: DamageType.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): DamageType.Type = json match {
      case JsString(str) => DamageType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object ActiveEffectTypeFormat extends RootJsonFormat[ActiveEffectType.Type] {
    override def write(obj: ActiveEffectType.Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): ActiveEffectType.Type = json match {
      case JsString(str) => ActiveEffectType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  implicit object ActionTypeFormat extends RootJsonFormat[ActionType.Type] {
    override def write(obj: Type): JsValue = JsString(obj.toString)

    override def read(json: JsValue): Type = json match {
      case JsString(str) => ActionType.withName(str)
      case unknown => deserializationError(s"json deserialize error: $unknown")
    }
  }

  // EFFECTS
  implicit val passiveEffectProtocol: RootJsonFormat[PassiveEffect] = jsonFormat2(PassiveEffect)

  implicit object OneTimeActiveEffectFormat extends RootJsonFormat[OneTimeActiveEffect] {
    override def write(obj: OneTimeActiveEffect): JsValue = {
      JsObject(
        "target" -> obj.target.toJson,
        "chance" -> JsNumber(obj.chance),
        "change" -> JsNumber(obj.change),
        "self" -> JsBoolean(obj.self),
        "_type" -> obj._type.toJson
      )
    }

    override def read(json: JsValue): OneTimeActiveEffect = {
      val fields = json.asJsObject.fields
      OneTimeActiveEffect(
        fields("target").convertTo[EffectTargetType.Type],
        fields("chance").convertTo[Double],
        fields("change").convertTo[Int],
        fields("self").convertTo[Boolean]
      )
    }
  }

  implicit object PeriodicActiveEffectFormat extends RootJsonFormat[PeriodicActiveEffect] {
    override def write(obj: PeriodicActiveEffect): JsValue = {
      JsObject(
        "target" -> obj.target.toJson,
        "chance" -> JsNumber(obj.chance),
        "change" -> JsNumber(obj.change),
        "self" -> JsBoolean(obj.self),
        "ticks" -> JsNumber(obj.ticks),
        "tickCd" -> JsNumber(obj.tickCd),
        "_type" -> obj._type.toJson
      )
    }

    override def read(json: JsValue): PeriodicActiveEffect = {
      val fields = json.asJsObject.fields
      PeriodicActiveEffect(
        fields("target").convertTo[EffectTargetType.Type],
        fields("chance").convertTo[Double],
        fields("change").convertTo[Int],
        fields("self").convertTo[Boolean],
        fields("ticks").convertTo[Int],
        fields("tickCd").convertTo[Int]
      )
    }
  }

  implicit object LastingActiveEffectFormat extends RootJsonFormat[LastingActiveEffect] {
    override def write(obj: LastingActiveEffect): JsValue = {
      JsObject(
        "target" -> obj.target.toJson,
        "chance" -> JsNumber(obj.chance),
        "change" -> JsNumber(obj.change),
        "self" -> JsBoolean(obj.self),
        "duration" -> JsNumber(obj.duration),
        "_type" -> obj._type.toJson
      )
    }

    override def read(json: JsValue): LastingActiveEffect = {
      val fields = json.asJsObject.fields
      LastingActiveEffect(
        fields("target").convertTo[EffectTargetType.Type],
        fields("chance").convertTo[Double],
        fields("change").convertTo[Int],
        fields("self").convertTo[Boolean],
        fields("duration").convertTo[Int]
      )
    }
  }

  implicit object ActiveEffectFormat extends RootJsonFormat[ActiveEffect] {
    override def write(obj: ActiveEffect): JsValue = obj match {
      case o: OneTimeActiveEffect => o.toJson
      case p: PeriodicActiveEffect => p.toJson
      case l: LastingActiveEffect => l.toJson
    }

    override def read(json: JsValue): ActiveEffect = {
      val effectType = json.asJsObject.getFields("_type") match {
        case Seq(JsString(m)) => ActiveEffectType.withName(m)
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }

      effectType match {
        case ActiveEffectType.OneTime => json.convertTo[OneTimeActiveEffect]
        case ActiveEffectType.Periodic => json.convertTo[PeriodicActiveEffect]
        case ActiveEffectType.Lasting => json.convertTo[LastingActiveEffect]
      }
    }
  }

  // ARMOR
  class ArmorTypedProtocol[T <: Armor](
    factory: (String, Int, Int, List[PassiveEffect], List[ActiveEffect]) => T
  ) extends RootJsonFormat[T] {
    override def write(obj: T): JsValue = {
      JsObject(
        "name" -> JsString(obj.name),
        "cd" -> JsNumber(obj.cd),
        "armor" -> JsNumber(obj.armor),
        "armorType" -> obj.armorType.toJson,
        "_type" -> obj._type.toJson,
        "passiveEffects" -> obj.passiveEffects.toJson,
        "activeEffects" -> obj.activeEffects.toJson
      )
    }

    override def read(json: JsValue): T = {
      val fields = json.asJsObject.fields
      factory(
        fields("name").convertTo[String],
        fields("cd").convertTo[Int],
        fields("armor").convertTo[Int],
        fields("passiveEffects").convertTo[List[PassiveEffect]],
        fields("activeEffects").convertTo[List[ActiveEffect]]
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
        case s: Shield => s.toJson
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
        case ArmorType.Shield => json.convertTo[Shield]
      }
    }
  }

  // WEAPON
  class WeaponTypedProtocol[T <: Weapon](
    factory: (String, Int, Int, Boolean, DamageType.Type, List[PassiveEffect], List[ActiveEffect]) => T
  ) extends RootJsonFormat[T] {
    override def write(obj: T): JsValue = {
      JsObject(
        "name" -> JsString(obj.name),
        "cd" -> JsNumber(obj.cd),
        "baseDamage" -> JsNumber(obj.baseDamage),
        "twoHanded" -> JsBoolean(obj.twoHanded),
        "damageType" -> obj.damageType.toJson,
        "weaponType" -> obj.weaponType.toJson,
        "_type" -> obj._type.toJson,
        "passiveEffects" -> obj.passiveEffects.toJson,
        "activeEffects" -> obj.activeEffects.toJson
      )
    }

    override def read(json: JsValue): T = {
      val fields = json.asJsObject.fields
      factory(
        fields("name").convertTo[String],
        fields("cd").convertTo[Int],
        fields("baseDamage").convertTo[Int],
        fields("twoHanded").convertTo[Boolean],
        fields("damageType").convertTo[DamageType.Type],
        fields("passiveEffects").convertTo[List[PassiveEffect]],
        fields("activeEffects").convertTo[List[ActiveEffect]]
      )
    }
  }
  implicit val swordProtocol: RootJsonFormat[Sword] = new WeaponTypedProtocol[Sword](Sword.apply)
  implicit val polearmProtocol: RootJsonFormat[Polearm] = new WeaponTypedProtocol[Polearm](Polearm.apply)
  implicit object daggerProtocol extends WeaponTypedProtocol[Dagger](
    (str, int1, int2, bool, damageType,  pe, ae) => items.Dagger(str, int1, int2, damageType, pe, ae)
  ) {
    override def read(json: JsValue): Dagger = {
      val fields = json.asJsObject.fields
      Dagger.apply(
        fields("name").convertTo[String],
        fields("cd").convertTo[Int],
        fields("baseDamage").convertTo[Int],
        fields("damageType").convertTo[DamageType.Type],
        fields("passiveEffects").convertTo[List[PassiveEffect]],
        fields("activeEffects").convertTo[List[ActiveEffect]]
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

    override def read(json: JsValue): Item = {
      val fields = json.asJsObject.fields
      val itemType = fields.get("_type") match {
        case Some(JsString(s)) => ItemType.withName(s)
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }

      itemType match {
        case ItemType.Weapon => json.convertTo[Weapon]
        case ItemType.Armor => json.convertTo[Armor]
      }
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
        "_type" -> obj._type.toJson
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
  implicit val initialPropertiesProtocol: RootJsonFormat[InitialProperties] = jsonFormat12(InitialProperties)
  implicit object PawnFormat extends RootJsonFormat[Pawn] {
    override def write(obj: Pawn): JsValue = {
      JsObject(
        "name" -> JsString(obj.name),
        "handle" -> obj.handle.toJson,
        "armorSet" -> obj.armorSet.toJson,
        "initProperties" -> obj.initProperties.toJson
      )
    }

    override def read(json: JsValue): Pawn = {
      val fields = json.asJsObject.fields
       Pawn(
         fields("name").convertTo[String],
         fields("handle").convertTo[Handle],
         fields("armorSet").convertTo[ArmorSet],
         fields("initProperties").convertTo[InitialProperties]
       )
    }
  }
  implicit object OpponentFormat extends RootJsonFormat[Opponent] {
    override def write(obj: Opponent): JsValue = {
      JsObject(
        "pawn" ->  obj.pawn.toJson,
        "hp" -> JsNumber(obj.hp),
        "activeEffects" -> obj.activeEffects.toJson
      )
    }

    override def read(json: JsValue): Opponent = {
      val fields = json.asJsObject.fields
      Opponent(
        fields("pawn").convertTo[Pawn],
        fields("hp").convertTo[Int],
        fields("activeEffects").convertTo[List[ActiveEffect]]
      )
    }
  }

  // ACTIONS
  implicit object DamageDealFormat extends RootJsonFormat[DamageDeal] {
    override def write(obj: DamageDeal): JsValue = {
      JsObject(
        "init" -> obj.init.toJson,
        "target" -> obj.target.toJson,
        "item" -> obj.item.toJson,
        "timestamp" -> obj.timestamp.toJson,
        "_type" -> obj._type.toJson,
        "damage" -> obj.damage.toJson
      )
    }

    override def read(json: JsValue): DamageDeal = {
      val fields = json.asJsObject.fields
      DamageDeal(
        fields("init").convertTo[Opponent],
        fields("target").convertTo[Opponent],
        fields("item").convertTo[Item],
        fields("timestamp").convertTo[Int],
        fields("damage").convertTo[Int]
      )
    }
  }

  implicit object AvoidanceFormat extends RootJsonFormat[Avoidance] {
    override def write(obj: Avoidance): JsValue = {
      JsObject(
        "init" -> obj.init.toJson,
        "target" -> obj.target.toJson,
        "item" -> obj.item.toJson,
        "timestamp" -> obj.timestamp.toJson,
        "_type" -> obj._type.toJson,
        "avoidanceType" -> obj.avoidanceType.toJson
      )
    }

    override def read(json: JsValue): Avoidance = {
      val fields = json.asJsObject.fields
      Avoidance(
        fields("init").convertTo[Opponent],
        fields("target").convertTo[Opponent],
        fields("item").convertTo[Item],
        fields("timestamp").convertTo[Int],
        fields("avoidanceType").convertTo[AvoidanceType.Type]
      )
    }
  }

  implicit object EffectApplicationFormat extends RootJsonFormat[EffectApplication] {
    override def write(obj: EffectApplication): JsValue = {
      JsObject(
        "init" -> obj.init.toJson,
        "target" -> obj.target.toJson,
        "item" -> obj.item.toJson,
        "timestamp" -> obj.timestamp.toJson,
        "_type" -> obj._type.toJson,
        "effect" -> obj.effect.toJson
      )
    }

    override def read(json: JsValue): EffectApplication = {
      val fields = json.asJsObject.fields
      EffectApplication(
        fields("init").convertTo[Opponent],
        fields("target").convertTo[Opponent],
        fields("item").convertTo[Item],
        fields("timestamp").convertTo[Int],
        fields("effect").convertTo[ActiveEffect]
      )
    }
  }

  implicit object EffectEndFormat extends RootJsonFormat[EffectEnd] {
    override def write(obj: EffectEnd): JsValue = {
      JsObject(
        "init" -> obj.init.toJson,
        "target" -> obj.target.toJson,
        "item" -> obj.item.toJson,
        "timestamp" -> obj.timestamp.toJson,
        "_type" -> obj._type.toJson,
        "effect" -> obj.effect.toJson
      )
    }

    override def read(json: JsValue): EffectEnd = {
      val fields = json.asJsObject.fields
      EffectEnd(
        fields("init").convertTo[Opponent],
        fields("target").convertTo[Opponent],
        fields("item").convertTo[Item],
        fields("timestamp").convertTo[Int],
        fields("effect").convertTo[ActiveEffect]
      )
    }
  }

  implicit object ActionFormat extends RootJsonFormat[Action] {
    override def write(obj: Action): JsValue =
      JsObject((obj match {
        case a: DamageDeal => a.toJson
        case a: Avoidance => a.toJson
        case ea: EffectApplication => ea.toJson
        case ee: EffectEnd => ee.toJson
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }).asJsObject.fields)

    override def read(json: JsValue): Action = {
      val fields = json.asJsObject.fields
      val actionType = fields.get("_type") match {
        case Some(JsString(s)) => ActionType.withName(s)
        case unknown => deserializationError(s"json deserialize error: $unknown")
      }
      actionType match {
        case ActionType.DamageDeal => json.convertTo[DamageDeal]
        case ActionType.Avoidance => json.convertTo[Avoidance]
        case ActionType.EffectApplication => json.convertTo[EffectApplication]
        case ActionType.EffectEnd => json.convertTo[EffectEnd]
      }
    }
  }
}
