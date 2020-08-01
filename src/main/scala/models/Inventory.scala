package models

import game.items.{ActiveEffect, PassiveEffect}
import models.UserModel
import util.MyPostgresProfile.api._

object InventoryModel extends TableQuery(new Inventory(_)) {

  def getUserInventory(user: User) =
    this.filter(_.userId === user.id).result

  def toDbItem(i: Inventory#TableElementType): DbItem =
    DbItem(i.id, i.name, i.cd, i._type, i.passiveEffects, i.activeEffects, i.user_id, i.armor, i.armorType, i.weaponType, i.baseDamage, i.twoHanded, i.damageType)
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

  def * =
    (id.?, name, cd, _type, passiveEffects, activeEffects, userId, armor.?, armorType.?, weaponType.?, baseDamage.?, twoHanded.?, damageType.?)
      .mapTo[DbItem]

  def user = foreignKey("inventory_fk", userId, UserModel)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
}

case class DbItem(
  id: Option[Int],
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
