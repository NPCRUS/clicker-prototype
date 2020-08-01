package models

import config.AppConfig
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

object UserModel extends TableQuery(new Users(_)){
  def getUserByUserId(userId: Int) = this.filter(_.userId === userId)
    .result
    .headOption

  def createFromToken(token: Token): Future[User] = {
    AppConfig.db.run(
      (this returning this.map(_.id)
        into ((user, id) => user.copy(id = Some(id)))
        ) += User(None, token.opaque_user_id, token.channel_id, token.role, token.is_unlinked, token.user_id.toInt)
    )
  }

  def toUser(u: Users#TableElementType): User =
    User(u.id, u.opaqueUserId, u.chanelId, u.role, u.isUnlinked, u.userId)
}

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

case class User(
  id: Option[Int],
  opaqueUserId: String,
  chanelId: String,
  role: String,
  isUnlinked: Boolean,
  userId: Int,
  maxMapLevel: Int = 1
)
