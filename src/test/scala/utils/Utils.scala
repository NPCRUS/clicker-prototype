package utils

import java.util.Base64

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import javax.crypto.spec.SecretKeySpec
import models.JsonSupport.{PubSubPermsProtocol, TokenProtocol}
import models.{PubSubPerms, Token}
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}
import spray.json.{DefaultJsonProtocol, RootJsonFormat, enrichAny}

object Utils extends App {

  def generateAuthToken(): Unit = {
    val secretBase64Array = Base64.getUrlDecoder.decode(AppConfig.config.getString("twitch-secret"))
    val secretKeyBase64 = new SecretKeySpec(secretBase64Array, "HmacSHA256")
    val token = Token(
      exp = 1919885508,
      opaque_user_id = "testing_opaque_user_id",
      user_id = "1",
      channel_id = "some_channel",
      role = "broadcaster",
      is_unlinked = false,
      pubsub_perms = PubSubPerms(Some(List("test")), Some(List("test")))
    )

    println(JwtSprayJson.encode(token.toJson.toString, secretKeyBase64, JwtAlgorithm.HS256))
  }

  generateAuthToken()
}
