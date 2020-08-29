package util

import java.util.Base64

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive1, Directives}
import javax.crypto.spec.SecretKeySpec
import models.JsonSupport._
import models.Token
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}

import scala.util.{Failure, Success}

object Authenticate extends Directives with SprayJsonSupport {

  val customAuthorization: Directive1[Token] = {
    optionalHeaderValueByName("Authorization") flatMap {
      case Some(authHeader) =>
        val accessToken = authHeader.split(' ').last
        val secretBase64Array = Base64.getUrlDecoder.decode(AppConfig.config.getString("twitch-secret"))
        val secretKeyBase64 = new SecretKeySpec(secretBase64Array, "HmacSHA256")
        JwtSprayJson.decodeJson(accessToken, secretKeyBase64, Seq(JwtAlgorithm.HS256)) match {
          case Success(value) =>
            try {
              val token = value.convertTo[Token]
              provide(token)
            } catch {
              case _: Throwable => complete(StatusCodes.Unauthorized, "jwt token payload is malformed")
            }
          case Failure(_) =>
            complete(StatusCodes.Unauthorized, "jwt token error")
        }
      case _ => complete(StatusCodes.Unauthorized, "authorization header is not readable")
    }
  }
}
