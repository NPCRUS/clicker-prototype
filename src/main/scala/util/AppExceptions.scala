package util

import akka.http.scaladsl.model.{HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._

import scala.util.Failure


object AppExceptions {
  def convertToHttpResponse(f: Throwable) = f match {
    case e: MapLevelExcessException => complete(e.statusCode, e.toString)
    case e: Throwable => complete(StatusCodes.InternalServerError, e.toString)
  }

  trait AppDefinedException extends Throwable {
    def statusCode: StatusCode
  }

  class MapLevelExcessException extends AppDefinedException {
    override def toString: String = "you cannot access this level"
    override def statusCode: StatusCode = StatusCodes.Conflict
  }

  class UserNotFound extends AppDefinedException {
    override def toString: String = "user not found"
    override def statusCode: StatusCode = StatusCodes.NotFound
  }
}
