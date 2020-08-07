package models

import scala.concurrent.Future
import config.AppConfig._
import util.MyPostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

object Transactions {
  def createUser(token: Token): Future[User] = {
    db.run(
      (for {
        user <- UserModel.createFromToken(token)
        _ <- CharacterModel.create(user.id.get)
      } yield user).transactionally
    )
  }
}
