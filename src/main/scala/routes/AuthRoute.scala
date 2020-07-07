package routes

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import JsonSupport._
import akka.http.scaladsl.model.StatusCodes
import javax.crypto.spec.SecretKeySpec
import pdi.jwt.{JwtAlgorithm, JwtSprayJson}

import scala.util.{Failure, Success}

class AuthRoute {
  def getRoutes: Route = path("auth") {
    concat {
      post {
        entity(as[RawToken])(validateToken)
      }
    }
  }

  def validateToken(rt: RawToken): Route = {
    val secretBase64Array = Base64.getUrlDecoder.decode(AppConfig.config.getString("twitch-secret"))
    val secretKeyBase64 = new SecretKeySpec(secretBase64Array, "HmacSHA256")
    val isValid = JwtSprayJson.isValid(rt.token, secretKeyBase64, Seq(JwtAlgorithm.HS256))
    if(isValid) complete(StatusCodes.OK)
    else complete(StatusCodes.Unauthorized)

//    JwtSprayJson.decodeJson(rt.token, secretKeyBase64, Seq(JwtAlgorithm.HS256)) match {
//      case Success(value) =>
//        try {
//          val token = value.convertTo[Token]
//          complete(token)
//        } catch {
//          case _: Throwable => complete(StatusCodes.Unauthorized, "malformed token")
//        }
//      case Failure(claim) => complete(StatusCodes.Unauthorized)
//    }
  }

}
