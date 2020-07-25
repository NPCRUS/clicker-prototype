package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import config.AppConfig
import models.{Schemas, User}
import models.JsonSupport._
import util.AppExceptions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class MeRoute {
  def getRoutes: Route = path("me") {
    get {
      Authenticate.customAuthorization{ token =>
        onComplete(
          AppConfig.db.run(Schemas.getUserByUserId(token.user_id.toInt)).flatMap {
            case Some(u) => Future(u)
            case None => Schemas.createFromToken(token)
          }.map { u =>
            User(u.id, u.opaqueUserId, u.chanelId, u.role, u.isUnlinked, u.userId)
          }
        ) {
          case Success(result) =>
            complete(result)
          case Failure(exception) =>
            complete(AppExceptions.convertToHttpResponse(exception))
        }
      }
    }
  }
}
