import JsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class MeRoute {
  def getRoutes: Route = path("me") {
    get {
      Authenticate.customAuthorization{ token =>
        onComplete(
          AppConfig.db.run(Schemas.getUserByOpaqueId(token.opaque_user_id)).flatMap {
            case Some(u) =>
              val user = User(u.id, u.opaqueUserId, u.chanelId, u.role, u.isUnlinked)
              Future(Some(user))
            case None => Future(None)
          }.flatMap {
            case Some(user) => Future(user)
            case None => Schemas.createFromToken(token)
          }
        ) {
          case Success(result) =>
            complete(result)
          case Failure(exception) =>
            complete(StatusCodes.InternalServerError)
        }
      }
    }
  }
}

//          AppConfig.db.run(
//            DBIO.seq(
//              Schemas.users += (None, token.opaque_user_id, token.channel_id, token.role, token.is_unlinked)
//            )
//          ).onComplete {
//            case Success(value) =>
//              println(value)
//              complete(StatusCodes.OK)
//            case Failure(exception) =>
//              println(exception)
//              complete(StatusCodes.InternalServerError)
//          }
