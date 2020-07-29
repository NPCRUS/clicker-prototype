package models

import config.AppConfig
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

object Schemas {
  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def opaqueUserId = column[String]("opaque_user_id")
    def channelId = column[String]("channel_id")
    def role = column[String]("role")
    def isUnlinked = column[Boolean]("is_unlinked")
    def userId = column[Int]("user_id")
    def maxMapLevel = column[Int]("max_map_level", O.Default(1))

    def * =
      (id.?, opaqueUserId, channelId, role, isUnlinked, userId, maxMapLevel).mapTo[User]
  }

  val users = TableQuery[Users]

  def getUserByUserId(userId: Int) = Schemas.users.filter(_.userId === userId)
    .result
    .headOption

  def createFromToken(token: Token): Future[User] = {
    AppConfig.db.run(
      (users returning Schemas.users.map(_.id)
        into ((user, id) => user.copy(id = Some(id)))
        ) += User(None, token.opaque_user_id, token.channel_id, token.role, token.is_unlinked, token.user_id.toInt)
    )
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

  val inventory = TableQuery[Inventory]

  def getUserInventory(user: User) =
    inventory.filter(_.userId === user.id).result
}

case class User(
  id: Option[Int],
  opaqueUserId: String,
  chanelId: String,
  role: String,
  isUnlinked: Boolean,
  userId: Int,
  maxMapLevel: Int = 1
)

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

