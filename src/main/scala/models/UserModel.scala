package models

import slick.dbio.Effect
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape
import slick.sql.{FixedSqlAction, SqlAction}

object UserModel extends TableQuery(new Users(_)) {
  def getUserByUserId(userId: Int): SqlAction[Option[User], NoStream, Effect.Read] = this.filter(_.userId === userId)
    .result
    .headOption

  def createFromToken(token: Token): FixedSqlAction[Int, NoStream, Effect.Write] = {
    (this returning this.map(_.id)) += User(0, token.opaque_user_id, token.channel_id, token.role, token.is_unlinked, token.user_id.toInt)
  }
}

class Users(tag: Tag) extends Table[User](tag, "users") {
  def * : ProvenShape[User] =
    (id, opaqueUserId, channelId, role, isUnlinked, userId, maxMapLevel).mapTo[User]

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def opaqueUserId = column[String]("opaque_user_id")

  def channelId = column[String]("channel_id")

  def role = column[String]("role")

  def isUnlinked = column[Boolean]("is_unlinked")

  def userId = column[Int]("user_id")

  def maxMapLevel = column[Int]("max_map_level", O.Default(1))
}

case class User(id: Int,
                opaqueUserId: String,
                channelId: String,
                role: String,
                isUnlinked: Boolean,
                userId: Int,
                maxMapLevel: Int = 1)
