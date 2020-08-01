package models

import config.AppConfig
import game.items.{ActiveEffect, PassiveEffect}
import util.MyPostgresProfile.api._

import scala.concurrent.Future

object InventoryModel extends TableQuery(new Inventory(_)) {

  def getUserInventory(user: User) =
    this.filter(_.userId === user.id).result

  def getItemByIdAndUser(id: Int, user: User) =
    this.filter(i => i.userId === user.id && i.id === id).result.headOption

  def getEquippedItems(user: User) =
    this.filter(i => i.userId === user.id && i.equipped).result

  def updateEquipped(items: List[DbItem], equip: Boolean) = {
    val ids = items.map(_.id.get)
    this
      .filter(_.id.inSet(ids))
      .map(_.equipped)
      .update(equip)
  }

  def insert(item: DbItem): Future[DbItem] = {
    AppConfig.db.run(
      (this returning this.map(_.id)
        into ((item, id) => item.copy(id = Some(id)))
        ) += item
    )
  }

  def toDbItem(i: Inventory#TableElementType): DbItem =
    DbItem(i.id, i.name, i.cd, i._type, i.passiveEffects, i.activeEffects, i.user_id, i.armor, i.armorType, i.weaponType, i.baseDamage, i.twoHanded, i.damageType, i.equipped)
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
  def equipped = column[Boolean]("equipped")

  def * =
    (id.?, name, cd, _type, passiveEffects, activeEffects, userId, armor.?, armorType.?, weaponType.?, baseDamage.?, twoHanded.?, damageType.?, equipped)
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
  damageType: Option[String],
  equipped: Boolean = false
)
