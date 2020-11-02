package models

import game.items.{ActiveEffect, Armor, Item, PassiveEffect, Weapon}
import slick.dbio.Effect
import slick.lifted.ProvenShape
import slick.sql.{FixedSqlStreamingAction, SqlAction}
import utils.AppConfig._
import utils.MyPostgresProfile.api._

import scala.concurrent.Future

object InventoryModel extends TableQuery(new Inventory(_)) {

  def getUserInventory(user: User): FixedSqlStreamingAction[Seq[DbItem], DbItem, Effect.Read] =
    this.filter(_.userId === user.id).result

  def getItemByIdAndUser(id: Int, user: User): SqlAction[Option[DbItem], NoStream, Effect.Read] =
    this.filter(i => i.userId === user.id && i.id === id).result.headOption

  def getItemsByIds(ids: Seq[Int]): FixedSqlStreamingAction[Seq[DbItem], DbItem, Effect.Read] =
    this.filter(_.id.inSet(ids)).result

  def insert(item: DbItem): Future[DbItem] = {
    db.run {
      (this returning this) += item
    }
  }
}

class Inventory(tag: Tag) extends Table[DbItem](tag, "inventory") {
  def * : ProvenShape[DbItem] =
    (id, name, cd, _type, passiveEffects, activeEffects, userId, armor.?, armorPart.?,armorType.?, weaponType.?, baseDamage.?, twoHanded.?, damageType.?, rarity)
      .mapTo[DbItem]

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def cd = column[Int]("cd")

  def _type = column[String]("_type")

  def passiveEffects = column[List[PassiveEffect]]("passive_effects")

  def activeEffects = column[List[ActiveEffect]]("active_effects")

  def armor = column[Int]("armor")

  def armorPart = column[String]("armor_part")

  def armorType = column[String]("armor_type")

  def weaponType = column[String]("weapon_type")

  def baseDamage = column[Int]("base_damage")

  def twoHanded = column[Boolean]("two_handed")

  def damageType = column[String]("damage_type")

  def rarity = column[String]("rarity")

  def user = foreignKey("inventory_fk", userId, UserModel)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

  def userId = column[Int]("user_id")
}

object DbItem {
  def fromGameItem(item: Item, user: User): DbItem = {
    def armorToDbItem(a: Armor, u: User): DbItem =
      DbItem(0, a.name, a.cd, a._type.toString, a.passiveEffects, a.activeEffects, u.id, Some(a.armor), Some(a.armorPart.toString), Some(a.armorType.toString), None, None, None, None, a.rarity.toString)

    def weaponToDbItem(w: Weapon, u: User): DbItem =
      DbItem(0, w.name, w.cd, w._type.toString, w.passiveEffects, w.activeEffects, u.id, None, None, None, Some(w.weaponType.toString), Some(w.baseDamage), Some(w.twoHanded), Some(w.damageType.toString), w.rarity.toString)

    item match {
      case a: Armor => armorToDbItem(a, user)
      case w: Weapon => weaponToDbItem(w, user)
    }
  }

  def tupled = (DbItem.apply _).tupled
}

case class DbItem(id: Int,
                  name: String,
                  cd: Int,
                  _type: String,
                  passiveEffects: List[PassiveEffect],
                  activeEffects: List[ActiveEffect],
                  user_id: Int,
                  armor: Option[Int],
                  armorPart: Option[String],
                  armorType: Option[String],
                  weaponType: Option[String],
                  baseDamage: Option[Int],
                  twoHanded: Option[Boolean],
                  damageType: Option[String],
                  rarity: String)
