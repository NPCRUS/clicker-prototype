package utils

import java.util.Base64

import javax.crypto.spec.SecretKeySpec
import models.JsonSupport.TokenProtocol
import models.{PubSubPerms, Token}
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtSprayJson}
import spray.json.enrichAny

object Utils {
  def generateAuthToken(): String = {
    val secretBase64Array = Base64.getUrlDecoder.decode(AppConfig.config.getString("twitch-secret"))
    val secretKeyBase64 = new SecretKeySpec(secretBase64Array, "HmacSHA256")
    val token = Token(100L, "", "", "", "", is_unlinked = false, PubSubPerms(None, None))
    val jwtClaim = JwtClaim(
      content = ???,
      issuer = ???,
      subject = ???,
      audience = ???,
      expiration = ???,
      notBefore = ???,
      issuedAt = ???,
      jwtId = ???
    )
    JwtSprayJson.encode(token.toJson.toString, secretKeyBase64, JwtAlgorithm.HS256)
  }
}
