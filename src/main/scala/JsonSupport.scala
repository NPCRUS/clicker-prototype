import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val RawTokenProtocol: RootJsonFormat[RawToken] = jsonFormat1(RawToken)
  implicit val PubSubPermsProtocol: RootJsonFormat[PubSubPerms] = jsonFormat2(PubSubPerms)
  implicit val TokenProtocol: RootJsonFormat[Token] = jsonFormat7(Token)

  implicit val UserProtocol: RootJsonFormat[User] = jsonFormat6(User)

}
