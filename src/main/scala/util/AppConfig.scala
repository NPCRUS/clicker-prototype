package util

import com.typesafe.config.{Config, ConfigFactory}
import slick.jdbc.H2Profile.api._

object AppConfig extends AppConfig

trait AppConfig {
  lazy val config: Config = ConfigFactory.load().getConfig("app")

  lazy val db = {
    Database.forConfig("app.db")
  }
}
