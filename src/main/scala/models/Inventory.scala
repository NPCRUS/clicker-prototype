package models

import models.UserModel.users
import slick.jdbc.H2Profile.api._

object Inventory {
  val inventory = TableQuery[Inventory]

  def getUserInventory(user: User) =
    inventory.filter(_.userId === user.id).result
}

class Inventory(tag: Tag) extends Table[DbItem](tag, "inventory") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def cd = column[Int]("cd")
  def _type = column[String]("_type")
  def passiveEffects = column[String]("passive_effects")
  def activeEffects = column[String]("active_effects")
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

  def user = foreignKey("inventory_fk", userId, users)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
}

case class DbItem(
  id: Option[Int],
  name: String,
  cd: Int,
  _type: String,
  passiveEffects: String,
  activeEffects: String,
  user_id: Int,
  armor: Option[Int],
  armorType: Option[String],
  weaponType: Option[String],
  baseDamage: Option[Int],
  twoHanded: Option[Boolean],
  damageType: Option[String]
)
