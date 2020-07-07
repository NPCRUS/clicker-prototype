package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import JsonSupport._
import akka.http.scaladsl.model.StatusCodes
import pdi.jwt.{JwtAlgorithm, JwtOptions, JwtSprayJson}

class AuthRoute {
  def getRoutes: Route = path("auth") {
    concat {
      post {
        entity(as[RawToken])(validateToken)
      }
    }
  }

  def validateToken(rt: RawToken): Route = {
    val secretKey = AppConfig.config.getString("twitch-secret")
    val isValid = JwtSprayJson.isValid(rt.token, secretKey, Seq(JwtAlgorithm.HS256))

    if(isValid) complete(StatusCodes.OK)
    else complete(StatusCodes.Unauthorized)
  }

}
