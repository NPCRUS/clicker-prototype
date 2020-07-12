import JsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

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
