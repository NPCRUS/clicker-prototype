package models

import game.items.{ActiveEffect, PassiveEffect}
import slick.dbio.Effect
import slick.lifted.ProvenShape
import slick.sql.{FixedSqlStreamingAction, SqlAction}
import util.AppConfig._
import util.MyPostgresProfile.api._

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
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def cd = column[Int]("cd")
  def _type = column[String]("_type")
  def passiveEffects = column[List[PassiveEffect]]("passive_effects")
  def activeEffects = column[List[ActiveEffect]]("active_effects")
  def userId = column[Int]("user_id")
  def armor = column[Int]("armor")
  def armorType = column[String]("armor_type")
  def weaponType = column[String]("weapon_type")
  def baseDamage = column[Int]("base_damage")
  def twoHanded = column[Boolean]("two_handed")
  def damageType = column[String]("damage_type")

  def * : ProvenShape[DbItem] =
    (id, name, cd, _type, passiveEffects, activeEffects, userId, armor.?, armorType.?, weaponType.?, baseDamage.?, twoHanded.?, damageType.?)
      .mapTo[DbItem]

  def user = foreignKey("inventory_fk", userId, UserModel)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
}

case class DbItem(
  id: Int,
  name: String,
  cd: Int,
  _type: String,
  passiveEffects: List[PassiveEffect],
  activeEffects: List[ActiveEffect],
  user_id: Int,
  armor: Option[Int],
  armorType: Option[String],
  weaponType: Option[String],
  baseDamage: Option[Int],
  twoHanded: Option[Boolean],
  damageType: Option[String]
)
