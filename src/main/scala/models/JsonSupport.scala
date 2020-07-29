package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val PubSubPermsProtocol: RootJsonFormat[PubSubPerms] = jsonFormat2(PubSubPerms)
  implicit val TokenProtocol: RootJsonFormat[Token] = jsonFormat7(Token)

  implicit val UserProtocol: RootJsonFormat[User] = jsonFormat7(User)
  implicit val DbItemProtocol: RootJsonFormat[DbItem] = jsonFormat13(DbItem)
  implicit val battlePostProtocol: RootJsonFormat[BattlePost] = jsonFormat1(BattlePost)

}
