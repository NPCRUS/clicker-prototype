package utils

import akka.http.scaladsl.model.{HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._

import scala.util.Failure


object AppExceptions {
  def apply(f: Throwable) = f match {
    case e: AppDefinedException =>
      println(e.toString)
      complete(e.statusCode, e.toString)
    case e: Throwable =>
      println(e.toString)
      complete(StatusCodes.InternalServerError, e.toString)
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

  class ItemNotFound extends UserNotFound {
    override def toString: String = "item not found"
  }

  class ItemIsAlreadyEquipped extends AppDefinedException {
    override def toString: String = "item is already equipped"
    override def statusCode: StatusCode = StatusCodes.Conflict
  }

  class EquipPartAndItemNotCompatible extends AppDefinedException {
    override def toString: String = "equip part and item are not compatible"
    override def statusCode: StatusCode = StatusCodes.BadRequest
  }
}
