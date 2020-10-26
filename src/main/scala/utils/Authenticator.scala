package utils

import java.util.Base64

import akka.http.scaladsl.server.Directives.{authenticateOAuth2, authenticateOAuth2Async}
import akka.http.scaladsl.server.directives.{AuthenticationDirective, Credentials}
import javax.crypto.spec.SecretKeySpec
import models.JsonSupport._
import models.{Token, Transactions, User, UserModel}
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class Authenticator extends AppConfig {

  val jwtAuth: AuthenticationDirective[Token] = authenticateOAuth2[Token]("ext", jwtAuthenticator)
  val jwtAuthWithUser: AuthenticationDirective[User] = authenticateOAuth2Async("ext", jwtWithUserAuthenticator)

  private def jwtWithUserAuthenticator(credentials: Credentials): Future[Option[User]] = {
    jwtAuthenticator(credentials) match {
      case Some(token) =>
        db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
          case Some(u) => Future(Some(u))
          case None => Transactions.createUser(token).map(Some(_))
        }
      case None => Future.successful(None)
    }
  }

  private def jwtAuthenticator(credentials: Credentials): Option[Token] =
    credentials match {
      case Credentials.Provided(jwt) =>
        val secretBase64Array = Base64.getUrlDecoder.decode(AppConfig.config.getString("twitch-secret"))
        val secretKeyBase64 = new SecretKeySpec(secretBase64Array, "HmacSHA256")
        JwtSprayJson.decodeJson(jwt, secretKeyBase64, Seq(JwtAlgorithm.HS256)) match {
          case Success(value) =>
            try {
              Some(value.convertTo[Token])
            } catch {
              case ex: Throwable =>
                println(s"$jwt jwt token payload is malformed, ${ex.toString}")
                None
            }
          case Failure(_) =>
            println(s"$jwt jwt token error")
            None
        }
      case Credentials.Missing => None
    }
}
