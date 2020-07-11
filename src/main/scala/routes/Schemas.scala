package routes

import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

object Schemas {
  class Users(tag: Tag) extends
    Table[User](tag, "users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def opaqueUserId = column[String]("opaque_user_id")
    def channelId = column[String]("channel_id")
    def role = column[String]("role")
    def isUnlinked = column[Boolean]("is_unlinked")

    def * = (id.?, opaqueUserId, channelId, role, isUnlinked) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]

  def getUserByOpaqueId(opaqueId: String) = Schemas.users.filter(_.opaqueUserId === opaqueId)
    .result
    .headOption

  def createFromToken(token: Token): Future[User] = {
    AppConfig.db.run(
      (Schemas.users returning Schemas.users.map(_.id)
        into ((user, id) => user.copy(id = Some(id)))
        ) += User(None, token.opaque_user_id, token.channel_id, token.role, token.is_unlinked)
    )
  }
}

case class User(
  id: Option[Int],
  opaqueUserId: String,
  chanelId: String,
  role: String,
  isUnlinked: Boolean
)

