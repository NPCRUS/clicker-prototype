package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import config.AppConfig
import models.JsonSupport._
import models.{Transactions, UserModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class MeRoute {
  def getRoutes: Route = path("me") {
    Authenticate.customAuthorization{ token =>
      get {
        onComplete(
          AppConfig.db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
            case Some(u) => Future(u)
            case None => Transactions.createUser(token)
          }.map { u =>
            UserModel.toUser(u)
          }
        ) {
          case Success(result) =>
            complete(result)
          case Failure(exception) => throw exception
        }
      }
    }
  }
}
